package com.ataglance.walletglance.categoryCollection.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.category.domain.Category
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithCategories
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionsWithCategories
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionsWithIdsByType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CategoryCollectionsViewModel(
    categoryList: List<Category>,
    categoryCollectionsWithIdsByType: CategoryCollectionsWithIdsByType
) : ViewModel() {

    private val _collectionType: MutableStateFlow<CategoryCollectionType> =
        MutableStateFlow(
            if (categoryCollectionsWithIdsByType.expense.isNotEmpty()) {
                CategoryCollectionType.Expense
            } else if (categoryCollectionsWithIdsByType.income.isNotEmpty()) {
                CategoryCollectionType.Income
            } else {
                CategoryCollectionType.Mixed
            }
        )
    val collectionType: StateFlow<CategoryCollectionType> = _collectionType.asStateFlow()

    private val _collectionsWithCategories: MutableStateFlow<CategoryCollectionsWithCategories> =
        MutableStateFlow(categoryCollectionsWithIdsByType.toCollectionsWithCategories(categoryList))
    private val collectionsWithCategories: StateFlow<CategoryCollectionsWithCategories> =
        _collectionsWithCategories.asStateFlow()

    val collectionsWithCategoriesByType: StateFlow<List<CategoryCollectionWithCategories>> =
        combine(
            _collectionsWithCategories,
            _collectionType
        ) { collectionsWithCategories, categoryType ->
            collectionsWithCategories.getByType(categoryType)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )


    fun changeCategoryType(type: CategoryCollectionType) {
        _collectionType.update { type }
    }

    fun getNewCollection(): CategoryCollectionWithCategories {
        return CategoryCollectionWithCategories(
            id = 0,
            orderNum = 0,
            type = collectionType.value,
            name = "",
            categoryList = emptyList()
        )
    }

    fun deleteCollection(collection: CategoryCollectionWithCategories) {
        _collectionsWithCategories.update {
            it.deleteCollection(collection)
        }
    }

    fun saveEditingCollection(editingCollection: CategoryCollectionWithCategories) {
        _collectionsWithCategories.update {
            if (editingCollection.id == 0) it.addCollection(editingCollection)
            else it.replaceCollection(editingCollection)
        }
    }

    fun getAllCollections(): List<CategoryCollectionWithCategories> {
        return collectionsWithCategories.value.concatenateLists()
    }

}

data class CategoryCollectionsViewModelFactory(
    private val categoryList: List<Category>,
    private val collectionsWithIds: CategoryCollectionsWithIdsByType
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryCollectionsViewModel(categoryList, collectionsWithIds) as T
    }
}
