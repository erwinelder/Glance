package com.ataglance.walletglance.category.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.presentation.model.CategoryStatisticsElementUiState
import com.ataglance.walletglance.category.presentation.model.CategoryStatisticsLists
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.utils.filterByCollection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CategoryStatisticsViewModel(
    passedCategoryCollections: CategoryCollectionsWithIdsByType,
    recordsFilteredByDateAndAccount: List<RecordStack>,
    categoryStatisticsLists: CategoryStatisticsLists,
    passedParentCategoryId: Int
) : ViewModel() {

    private val _parentCategoryId = MutableStateFlow(passedParentCategoryId.takeIf { it != 0 })
    val parentCategoryId = _parentCategoryId.asStateFlow()

    fun clearParentCategoryId() {
        _parentCategoryId.update { null }
    }

    private val _categoryCollectionsByType = MutableStateFlow(passedCategoryCollections)

    fun setCategoryCollections(collections: CategoryCollectionsWithIdsByType) {
        _categoryCollectionsByType.update { collections }
    }


    private val _recordsFilteredByDateAndAccount = MutableStateFlow(recordsFilteredByDateAndAccount)

    fun setRecordsFilteredByDateAndAccount(recordList: List<RecordStack>) {
        _recordsFilteredByDateAndAccount.update { recordList }
    }


    private val _categoryStatisticsByAccountAndDate = MutableStateFlow(categoryStatisticsLists)
    private val categoryStatisticsByAccountAndDate = _categoryStatisticsByAccountAndDate
        .asStateFlow()

    fun setCategoryStatisticsByAccountAndDate(statistics: CategoryStatisticsLists) {
        _categoryStatisticsByAccountAndDate.update { statistics }
    }


    private val _parentCategoryStatistics =
        MutableStateFlow<CategoryStatisticsElementUiState?>(null)
    val parentCategoryStatistics = _parentCategoryStatistics.asStateFlow()

    fun setParentCategoryStatistics() {
        parentCategoryId.value
            ?.let { categoryStatisticsByAccountAndDate.value.getItemByParentCategoryId(it) }
            ?.takeIf { it.subcategoriesStatisticsUiState != null }
            ?.let { parentCategory ->
                _parentCategoryStatistics.update { parentCategory }
            }
    }

    fun setParentCategoryStatistics(category: CategoryStatisticsElementUiState) {
        if (category.subcategoriesStatisticsUiState != null) {
            _parentCategoryStatistics.update { category }
        }
    }

    fun clearParentCategoryStatistics() {
        _parentCategoryStatistics.update { null }
    }


    private val _categoryType = MutableStateFlow(CategoryType.Expense)
    val categoryType = _categoryType.asStateFlow()

    fun setCategoryType(newCategoryType: CategoryType) {
        if (newCategoryType == categoryType.value) return

        if (parentCategoryStatistics.value != null) clearParentCategoryStatistics()
        _categoryType.update { newCategoryType }
        resetSelectedCollection()
    }


    val currentCollectionList = combine(
        _categoryCollectionsByType,
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
        if (parentCategoryStatistics.value != null) {
            clearParentCategoryStatistics()
        }
        _selectedCollection.update { collection }
    }

    private fun resetSelectedCollection() {
        _selectedCollection.update {
            _categoryCollectionsByType.value.getByCategoryType(categoryType.value).firstOrNull()
                ?: CategoryCollectionWithIds()
        }
    }


    val categoryStatisticsList: StateFlow<List<CategoryStatisticsElementUiState>> = combine(
        _recordsFilteredByDateAndAccount,
        _categoryStatisticsByAccountAndDate,
        _categoryType,
        _selectedCollection,
        _parentCategoryStatistics
    ) { recordsFilteredByDateAndAccount, categoryStatisticsByAccountAndDate, categoryType,
        selectedCollection, parentCategory ->

        parentCategory?.subcategoriesStatisticsUiState
            ?: selectedCollection.takeIf { it.hasLinkedCategories() }?.let {
                CategoryStatisticsLists
                    .fromRecordStacks(recordsFilteredByDateAndAccount.filterByCollection(it))
                    .getByType(categoryType)
            }
            ?: categoryStatisticsByAccountAndDate.getByType(categoryType)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = categoryStatisticsLists.expense
    )

}

class CategoryStatisticsViewModelFactory(
    private val categoryCollections: CategoryCollectionsWithIdsByType,
    private val recordsFilteredByDateAndAccount: List<RecordStack>,
    private val categoryStatisticsLists: CategoryStatisticsLists,
    private val parentCategoryId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryStatisticsViewModel(
            passedCategoryCollections = categoryCollections,
            recordsFilteredByDateAndAccount = recordsFilteredByDateAndAccount,
            categoryStatisticsLists = categoryStatisticsLists,
            passedParentCategoryId = parentCategoryId
        ) as T
    }
}
