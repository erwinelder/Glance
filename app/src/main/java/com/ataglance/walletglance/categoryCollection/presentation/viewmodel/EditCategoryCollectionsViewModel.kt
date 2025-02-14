package com.ataglance.walletglance.categoryCollection.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithCategories
import com.ataglance.walletglance.categoryCollection.domain.usecase.GetCategoryCollectionsUseCase
import com.ataglance.walletglance.categoryCollection.domain.usecase.SaveCategoryCollectionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditCategoryCollectionsViewModel(
    private val saveCategoryCollectionsUseCase: SaveCategoryCollectionsUseCase,
    private val getCategoryCollectionsUseCase: GetCategoryCollectionsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            val categories = getCategoriesUseCase.getAsList()
            val collections = getCategoryCollectionsUseCase.get().toCollectionsWithCategories(
                allCategories = categories
            )

            _collectionsWithCategories.update { collections }
            _collectionType.update {
                when {
                    collections.expense.isNotEmpty() -> CategoryCollectionType.Expense
                    collections.income.isNotEmpty() -> CategoryCollectionType.Income
                    else -> CategoryCollectionType.Mixed
                }
            }
        }
    }


    private val _collectionsWithCategories = MutableStateFlow(CategoryCollectionsWithCategories())


    private val _collectionType = MutableStateFlow(CategoryCollectionType.Expense)
    val collectionType = _collectionType.asStateFlow()

    fun changeCategoryType(type: CategoryCollectionType) {
        _collectionType.update { type }
    }


    val collectionsByType: StateFlow<List<CategoryCollectionWithCategories>> = combine(
        _collectionsWithCategories,
        _collectionType
    ) { collections, categoryType ->
        collections.getByType(categoryType)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )


    fun getNewCollection(): CategoryCollectionWithCategories {
        return CategoryCollectionWithCategories(
            id = 0,
            orderNum = 0,
            type = collectionType.value,
            name = "",
            categoryList = emptyList()
        )
    }

    fun applyCollection(collection: CategoryCollectionWithCategories) {
        _collectionsWithCategories.update {
            if (collection.id == 0) it.addCollection(collection)
            else it.replaceCollection(collection)
        }
    }

    fun deleteCollection(collection: CategoryCollectionWithCategories) {
        _collectionsWithCategories.update {
            it.deleteCollection(collection)
        }
    }


    suspend fun saveCategoryCollections() {
        saveCategoryCollectionsUseCase.execute(
            collections = _collectionsWithCategories.value.concatenateLists()
        )
    }

}