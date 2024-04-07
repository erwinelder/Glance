package com.ataglance.walletglance.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CategoryStatisticsViewModel(
    categoryStatisticsLists: CategoryStatisticsLists
) : ViewModel() {

    private val _categoryStatisticsLists = MutableStateFlow(categoryStatisticsLists)

    fun setCategoryStatisticsLists(newCategoryStatisticsLists: CategoryStatisticsLists) {
        _categoryStatisticsLists.update { newCategoryStatisticsLists }
    }

    private val _categoryType = MutableStateFlow(CategoryType.Expense)
    val categoryType = _categoryType.asStateFlow()

    fun setCategoryType(newCategoryType: CategoryType) {
        if (newCategoryType == categoryType.value) return

        if (parentCategory.value != null) clearParentCategory()
        _categoryType.update { newCategoryType }
    }

    private val _parentCategory = MutableStateFlow<CategoryStatisticsElementUiState?>(null)
    val parentCategory = _parentCategory.asStateFlow()

    fun setParentCategory(parentCategory: CategoryStatisticsElementUiState) {
        if (parentCategory.subcategoriesStatisticsUiState != null) {
            _parentCategory.update { parentCategory }
        }
    }

    fun setParentCategoryById(parentCategoryId: Int?) {
        if (parentCategoryId == null || parentCategoryId == 0) return

        categoryList.value.find { it.categoryId == parentCategoryId }?.let { setParentCategory(it) }
    }

    fun clearParentCategory() {
        _parentCategory.update { null }
    }

    val categoryList = combine(
        _categoryType,
        _parentCategory,
        _categoryStatisticsLists
    ) { categoryType, parentCategory, categoryStatisticsLists ->
        if (parentCategory?.subcategoriesStatisticsUiState != null) {
            parentCategory.subcategoriesStatisticsUiState
        } else {
            when (categoryType) {
                CategoryType.Expense -> categoryStatisticsLists.expense
                CategoryType.Income -> categoryStatisticsLists.income
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = categoryStatisticsLists.expense
    )

}

class CategoryStatisticsViewModelFactory(
    private val categoryStatisticsLists: CategoryStatisticsLists
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryStatisticsViewModel(categoryStatisticsLists) as T
    }
}
