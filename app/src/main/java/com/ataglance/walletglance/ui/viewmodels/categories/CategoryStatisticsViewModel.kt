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
    private val categoriesWithSubcategories: CategoriesWithSubcategories,
    passedCategoryCollections: CategoryCollectionsWithIds,
    recordsFilteredByDateAndAccount: List<RecordStack>,
    categoryStatisticsLists: CategoryStatisticsLists
) : ViewModel() {

    private val _categoryCollections = MutableStateFlow(passedCategoryCollections)
    val categoryCollections = _categoryCollections.asStateFlow()

    fun setCategoryCollections(collections: CategoryCollectionsWithIds) {
        _categoryCollections.update { collections }
    }


    private val _recordsFilteredByDateAndAccount = MutableStateFlow(recordsFilteredByDateAndAccount)

    fun setRecordsFilteredByDateAndAccount(recordList: List<RecordStack>) {
        _recordsFilteredByDateAndAccount.update { recordList }
    }


    private val _defaultCategoryStatisticsLists = MutableStateFlow(categoryStatisticsLists)

    fun setCategoryStatisticsLists(newCategoryStatisticsLists: CategoryStatisticsLists) {
        _defaultCategoryStatisticsLists.update { newCategoryStatisticsLists }
    }


    private val _parentCategoryStatistics =
        MutableStateFlow<CategoryStatisticsElementUiState?>(null)
    val parentCategoryStatistics = _parentCategoryStatistics.asStateFlow()

    fun setParentCategory(parentCategory: CategoryStatisticsElementUiState?) {
        if (parentCategory?.subcategoriesStatisticsUiState != null) {
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
        resetSelectedCollection()
    }


    val currentCollectionList = combine(
        _categoryCollections,
        _categoryType
    ) { collections, categoryType ->
        collections.getByCategoryType(categoryType)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _selectedCollection = MutableStateFlow(
        passedCategoryCollections.getByCategoryType(categoryType.value).firstOrNull()
            ?: CategoryCollectionWithIds()
    )
    val selectedCollection = _selectedCollection.asStateFlow()

    fun selectCollection(collection: CategoryCollectionWithIds) {
        if (parentCategoryStatistics.value != null) clearParentCategory()
        _selectedCollection.update { collection }
    }

    private fun resetSelectedCollection() {
        _selectedCollection.update {
            categoryCollections.value.getByCategoryType(categoryType.value).firstOrNull()
                ?: CategoryCollectionWithIds()
        }
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
    private val categoriesWithSubcategories: CategoriesWithSubcategories,
    private val categoryCollections: CategoryCollectionsWithIds,
    private val recordsFilteredByDateAndAccount: List<RecordStack>,
    private val categoryStatisticsLists: CategoryStatisticsLists
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryStatisticsViewModel(
            categoriesWithSubcategories = categoriesWithSubcategories,
            passedCategoryCollections = categoryCollections,
            recordsFilteredByDateAndAccount = recordsFilteredByDateAndAccount,
            categoryStatisticsLists = categoryStatisticsLists
        ) as T
    }
}
