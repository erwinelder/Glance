package com.ataglance.walletglance.presentation.viewmodels.categoryCollections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.EditingCategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.EditingCategoryWithSubcategories
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithCategories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class EditCategoryCollectionViewModel(
    private val categoriesWithSubcategories: CategoriesWithSubcategories
) : ViewModel() {

    private val _collectionUiState: MutableStateFlow<CategoryCollectionWithCategories> =
        MutableStateFlow(CategoryCollectionWithCategories())
    val collectionUiState: StateFlow<CategoryCollectionWithCategories> =
        _collectionUiState.asStateFlow()

    private val _editingCategoriesWithSubcategories:
            MutableStateFlow<EditingCategoriesWithSubcategories> =
        MutableStateFlow(EditingCategoriesWithSubcategories())
    val editingCategoriesWithSubcategories: StateFlow<EditingCategoriesWithSubcategories> =
        _editingCategoriesWithSubcategories.asStateFlow()

    val expandedCategory: StateFlow<EditingCategoryWithSubcategories?> = combine(
        _editingCategoriesWithSubcategories
    ) { editingCategoriesWithSubcategoriesArray ->
        editingCategoriesWithSubcategoriesArray[0].concatenateLists().find { it.expanded }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    val allowSaving: StateFlow<Boolean> = combine(
        _collectionUiState,
        _editingCategoriesWithSubcategories
    ) { collection, editingCategoriesWithSubcategories ->
        collection.name.isNotBlank() && editingCategoriesWithSubcategories.hasCheckedCategory()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    private val _allowDeleting: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val allowDeleting: StateFlow<Boolean> = _allowDeleting.asStateFlow()


    fun applyCollection(collection: CategoryCollectionWithCategories) {
        _collectionUiState.update {
            collection
        }
        _allowDeleting.update {
            collection.orderNum != 0
        }
        _editingCategoriesWithSubcategories.update {
            categoriesWithSubcategories.toEditingCategoriesWithSubcategories(collection)
        }
    }

    fun changeName(name: String) {
        _collectionUiState.update { it.copy(name = name) }
    }

    fun inverseCheckedCategoryState(category: Category) {
        _editingCategoriesWithSubcategories.update { it.inverseCheckedCategoryState(category) }
    }

    fun inverseExpandedState(category: Category) {
        _editingCategoriesWithSubcategories.update { it.inverseExpandedState(category) }
    }

    fun getCollection(): CategoryCollectionWithCategories {
        return collectionUiState.value.let {
            it.copy(
                name = it.name.trim(),
                categoryList = editingCategoriesWithSubcategories.value.getCheckedCategories()
            )
        }
    }

}

data class EditCategoryCollectionViewModelFactory(
    private val categoriesWithSubcategories: CategoriesWithSubcategories
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditCategoryCollectionViewModel(categoriesWithSubcategories) as T
    }
}