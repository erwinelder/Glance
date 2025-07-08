package com.ataglance.walletglance.categoryCollection.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.category.mapper.toCheckedCategoriesWithSubcategories
import com.ataglance.walletglance.category.presentation.model.CheckedGroupedCategories
import com.ataglance.walletglance.category.presentation.model.CheckedGroupedCategoriesByType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditCategoryCollectionViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            groupedCategoriesByType = getCategoriesUseCase.getGrouped()
        }
    }


    private var groupedCategoriesByType = GroupedCategoriesByType()


    private val _collectionUiState = MutableStateFlow(CategoryCollectionWithCategories())
    val collectionUiState = _collectionUiState.asStateFlow()

    private val _checkedGroupedCategoriesByType = MutableStateFlow(
        CheckedGroupedCategoriesByType()
    )
    val checkedGroupedCategoriesByType = _checkedGroupedCategoriesByType.asStateFlow()

    val expandedCategory: StateFlow<CheckedGroupedCategories?> = combine(
        _checkedGroupedCategoriesByType
    ) { editingCategoriesWithSubcategoriesArray ->
        editingCategoriesWithSubcategoriesArray[0].concatenateLists().find { it.expanded }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )


    fun applyCollection(collection: CategoryCollectionWithCategories) {
        _collectionUiState.update {
            collection
        }
        _checkedGroupedCategoriesByType.update {
            groupedCategoriesByType.toCheckedCategoriesWithSubcategories(collection)
        }
    }

    fun changeName(name: String) {
        _collectionUiState.update { it.copy(name = name) }
    }

    fun inverseCheckedCategoryState(category: Category) {
        _checkedGroupedCategoriesByType.update { it.inverseCheckedCategoryState(category) }
    }

    fun inverseExpandedState(category: Category) {
        _checkedGroupedCategoriesByType.update { it.inverseExpandedState(category) }
    }


    val allowSaving = combine(
        _collectionUiState,
        _checkedGroupedCategoriesByType
    ) { collection, editingCategoriesWithSubcategories ->
        collection.allowSaving() && editingCategoriesWithSubcategories.hasCheckedCategory()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

    val allowDeleting = combine(_collectionUiState) { collection ->
        collection[0].orderNum != 0
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )


    fun getCollection(): CategoryCollectionWithCategories {
        return collectionUiState.value.let {
            it.copy(
                name = it.name.trim(),
                categories = checkedGroupedCategoriesByType.value.getCheckedCategories()
            )
        }
    }

}