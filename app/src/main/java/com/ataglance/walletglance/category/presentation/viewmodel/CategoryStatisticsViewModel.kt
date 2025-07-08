package com.ataglance.walletglance.category.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.category.mapper.toCategoryCollectionType
import com.ataglance.walletglance.category.presentation.model.CategoriesStatistics
import com.ataglance.walletglance.category.presentation.model.CategoryStatistics
import com.ataglance.walletglance.category.presentation.model.GroupedCategoryStatistics
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.domain.usecase.GetCategoryCollectionsUseCase
import com.ataglance.walletglance.categoryCollection.domain.utils.toggleExpenseIncome
import com.ataglance.walletglance.categoryCollection.presentation.model.CategoryCollectionsUiState
import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.transaction.domain.usecase.GetTransactionsInDateRangeUseCase
import com.ataglance.walletglance.transaction.domain.utils.filterByAccount
import com.ataglance.walletglance.transaction.domain.utils.filterByCollection
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
    activeDateRange: TimestampRange,
    private val defaultCollectionName: String,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getCategoryCollectionsUseCase: GetCategoryCollectionsUseCase,
    private val getTransactionsInDateRangeUseCase: GetTransactionsInDateRangeUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {

            groupedCategoriesByType = getCategoriesUseCase.getGrouped()

            getCategoryCollectionsUseCase.getFlow().collect { collections ->
                collectionsByType = collections
                setCategoryCollections(collections = collections)
            }

        }
    }


    private var initialParentCategoryId: Int? = initialCategoryId

    private var groupedCategoriesByType = GroupedCategoriesByType()


    private val _activeAccount = MutableStateFlow(activeAccount)

    fun setActiveAccount(account: Account?) {
        if (_activeAccount.value == account) return

        _activeAccount.update { account }
        clearParentCategoryStatistics()
    }


    private val _activeDateRange = MutableStateFlow(activeDateRange)

    fun setActiveDateRange(dateRange: TimestampRange) {
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
    private val _transactionsInDateRange = _activeDateRange.flatMapLatest { dateRange ->
        getTransactionsInDateRangeUseCase.getAsFlowOrEmpty(range = dateRange)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    private val categoriesStatistics: StateFlow<List<CategoryStatistics>> = combine(
        _transactionsInDateRange,
        _activeAccount,
        _categoryCollectionsUiState
    ) { transactions, account, collectionsState ->
        if (account == null) return@combine emptyList()

        val categoryType = when (collectionsState.activeType) {
            CategoryCollectionType.Expense -> CategoryType.Expense
            CategoryCollectionType.Income -> CategoryType.Income
            CategoryCollectionType.Mixed -> CategoryType.Expense
        }

        val statistics = transactions
            .filterByAccount(accountId = account.id)
            .filterByCollection(collection = collectionsState.activeCollection)
            .let { transactions ->
                CategoriesStatistics.fromTransactions(
                    type = categoryType,
                    accountId = account.id,
                    accountCurrency = account.currency,
                    transactions = transactions,
                    groupedCategories = groupedCategoriesByType.getByType(type = categoryType)
                )
            }

        initialParentCategoryId?.let { id ->
            statistics.getParentStatsIfSubStatsPresent(categoryId = id)?.let { statistics ->
                _parentCategoryStatistics.update { statistics }
            }
        }

        statistics.stats
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
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
        categoriesStatistics
    ) { parentCategory, statistics ->
        GroupedCategoryStatistics(
            parentCategory = parentCategory,
            subcategories = parentCategory?.subcategoriesStatistics ?: statistics
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GroupedCategoryStatistics()
    )

}