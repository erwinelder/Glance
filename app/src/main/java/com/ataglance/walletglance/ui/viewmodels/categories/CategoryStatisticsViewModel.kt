package com.ataglance.walletglance.ui.viewmodels.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.CategoryStatisticsElementUiState
import com.ataglance.walletglance.data.categories.CategoryStatisticsLists
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithIds
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionsWithIds
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.ui.utils.filterByCollection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CategoryStatisticsViewModel(
    private val categoryCollections: CategoryCollectionsWithIds,
    private val categoriesWithSubcategories: CategoriesWithSubcategories,
    recordsFilteredByDateAndAccount: List<RecordStack>,
    categoryStatisticsLists: CategoryStatisticsLists,
    parentCategoryId: Int
) : ViewModel() {

    private val _recordsFilteredByDateAndAccount = MutableStateFlow(recordsFilteredByDateAndAccount)

    fun setRecordsFilteredByDateAndAccount(recordList: List<RecordStack>) {
        _recordsFilteredByDateAndAccount.update { recordList }
    }


    private val _defaultCategoryStatisticsLists = MutableStateFlow(categoryStatisticsLists)

    fun setCategoryStatisticsLists(newCategoryStatisticsLists: CategoryStatisticsLists) {
        _defaultCategoryStatisticsLists.update { newCategoryStatisticsLists }
    }


    private val _parentCategoryStatistics = MutableStateFlow(
        categoryStatisticsLists.getItemByParentCategoryId(parentCategoryId)
    )
    val parentCategoryStatistics = _parentCategoryStatistics.asStateFlow()

    fun setParentCategory(parentCategory: CategoryStatisticsElementUiState) {
        if (parentCategory.subcategoriesStatisticsUiState != null) {
            _parentCategoryStatistics.update { parentCategory }
        }
    }

    fun clearParentCategory() {
        _parentCategoryStatistics.update { null }
    }


    private val _categoryType = MutableStateFlow(CategoryType.Expense)
    val categoryType = _categoryType.asStateFlow()

    fun setCategoryType(newCategoryType: CategoryType) {
        if (newCategoryType == categoryType.value) return

        if (parentCategoryStatistics.value != null) clearParentCategory()
        _categoryType.update { newCategoryType }
    }


    val currentCollectionList = combine(_categoryType) { categoryTypeArray ->
        categoryCollections.getByCategoryType(categoryTypeArray[0])
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _selectedCollection = MutableStateFlow(
        categoryCollections.getByCategoryType(categoryType.value).firstOrNull()
            ?: CategoryCollectionWithIds()
    )
    val selectedCollection = _selectedCollection.asStateFlow()

    fun selectCollection(collection: CategoryCollectionWithIds) {
        _selectedCollection.update { collection }
    }


    val categoryStatisticsList = combine(
        _categoryType,
        _parentCategoryStatistics,
        _recordsFilteredByDateAndAccount,
        _defaultCategoryStatisticsLists,
        _selectedCollection
    ) { categoryType, parentCategory, recordsFilteredByDateAndAccount,
        defaultCategoryStatisticsLists, selectedCollection ->
        parentCategory?.subcategoriesStatisticsUiState
            ?: selectedCollection
                .takeIf { it.categoriesIds?.isNotEmpty() == true }
                ?.let {
                    categoriesWithSubcategories.getStatistics(
                        recordStackList = recordsFilteredByDateAndAccount.filterByCollection(it)
                    ).getByType(categoryType)
                }
            ?: defaultCategoryStatisticsLists.getByType(categoryType)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = categoryStatisticsLists.expense
    )

}

class CategoryStatisticsViewModelFactory(
    private val categoryCollections: CategoryCollectionsWithIds,
    private val categoriesWithSubcategories: CategoriesWithSubcategories,
    private val recordsFilteredByDateAndAccount: List<RecordStack>,
    private val categoryStatisticsLists: CategoryStatisticsLists,
    private val parentCategoryId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryStatisticsViewModel(
            categoryCollections = categoryCollections,
            categoriesWithSubcategories = categoriesWithSubcategories,
            recordsFilteredByDateAndAccount = recordsFilteredByDateAndAccount,
            categoryStatisticsLists = categoryStatisticsLists,
            parentCategoryId = parentCategoryId
        ) as T
    }
}
