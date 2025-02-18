package com.ataglance.walletglance.category.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.mapper.toCategoryCollectionType
import com.ataglance.walletglance.category.presentation.model.CategoriesStatisticsByType
import com.ataglance.walletglance.category.presentation.model.CategoryStatistics
import com.ataglance.walletglance.category.presentation.model.GroupedCategoryStatistics
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.domain.usecase.GetCategoryCollectionsUseCase
import com.ataglance.walletglance.categoryCollection.domain.utils.toggleExpenseIncome
import com.ataglance.walletglance.categoryCollection.presentation.model.CategoryCollectionsUiState
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.record.domain.usecase.GetRecordStacksInDateRangeUseCase
import com.ataglance.walletglance.record.domain.utils.filterByAccount
import com.ataglance.walletglance.record.domain.utils.filterByCollection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryStatisticsViewModel(
    initialCategoryId: Int?,
    initialCategoryType: CategoryType,
    activeAccount: Account?,
    activeDateRange: LongDateRange,
    private val defaultCollectionName: String,
    private val getCategoryCollectionsUseCase: GetCategoryCollectionsUseCase,
    private val getRecordStacksInDateRangeUseCase: GetRecordStacksInDateRangeUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {

            getCategoryCollectionsUseCase.getFlow().collect { collections ->
                collectionsByType = collections
                setCategoryCollections(collections = collections)
            }

        }
    }


    private var initialParentCategoryId: Int? = initialCategoryId


    private val _activeAccountId = MutableStateFlow(activeAccount?.id)

    fun setActiveAccountId(id: Int) {
        if (_activeAccountId.value == id) return

        _activeAccountId.update { id }
        clearParentCategoryStatistics()
    }


    private val _activeDateRange = MutableStateFlow(activeDateRange)

    fun setActiveDateRange(dateRange: LongDateRange) {
        if (_activeDateRange.value.equalsTo(dateRange)) return

        _activeDateRange.update { dateRange }
        clearParentCategoryStatistics()
    }


    private var collectionsByType = CategoryCollectionsWithIdsByType()

    private val _categoryCollectionsUiState = MutableStateFlow(
        CategoryCollectionsUiState(activeType = initialCategoryType.toCategoryCollectionType())
    )
    val categoryCollectionsUiState = _categoryCollectionsUiState.asStateFlow()

    private fun setCategoryCollections(collections: CategoryCollectionsWithIdsByType) {
        _categoryCollectionsUiState.update {
            it.setCollections(
                collections = collections, defaultCollectionName = defaultCollectionName
            )
        }
    }

    fun toggleCollectionType() {
        _categoryCollectionsUiState.update {
            it.changeCollectionType(
                type = it.activeType.toggleExpenseIncome(),
                collectionsByType = collectionsByType,
                defaultCollectionName = defaultCollectionName
            )
        }
        if (_parentCategoryStatistics.value != null) {
            clearParentCategoryStatistics()
        }
    }

    fun selectCollection(collectionId: Int) {
        _categoryCollectionsUiState.update {
            it.selectCollection(collectionId = collectionId)
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private val _recordsInDateRange = _activeDateRange.flatMapLatest { dateRange ->
        getRecordStacksInDateRangeUseCase.getFlow(range = dateRange)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )


    private val statisticsByType: StateFlow<CategoriesStatisticsByType> = combine(
        _recordsInDateRange,
        _activeAccountId,
        _categoryCollectionsUiState
    ) { stacks, accountId, collectionsState ->
        val statistics = stacks.filterByAccount(accountId)
            .filterByCollection(collectionsState.activeCollection)
            .let { CategoriesStatisticsByType.fromRecordStacks(it) }

        initialParentCategoryId?.let { id ->
            statistics.getParentStatsIfSubStatsPresent(id)?.let { statistics ->
                _parentCategoryStatistics.update { statistics }
            }
        }

        statistics
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = CategoriesStatisticsByType()
    )


    private val _parentCategoryStatistics = MutableStateFlow<CategoryStatistics?>(null)

    fun setParentCategoryStatistics(statistics: CategoryStatistics) {
        if (statistics.subcategoriesStatistics != null) {
            _parentCategoryStatistics.update { statistics }
            initialParentCategoryId = null
        }
    }

    fun clearParentCategoryStatistics() {
        _parentCategoryStatistics.update { null }
        initialParentCategoryId = null
    }


    val groupedCategoryStatistics: StateFlow<GroupedCategoryStatistics> = combine(
        _parentCategoryStatistics,
        _categoryCollectionsUiState,
        statisticsByType
    ) { parentCategory, collectionsState, statistics ->
        GroupedCategoryStatistics(
            parentCategory = parentCategory,
            subcategories = parentCategory?.subcategoriesStatistics
                ?: statistics.getByType(collectionsState.activeType)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = GroupedCategoryStatistics()
    )

}