package com.ataglance.walletglance.category.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.usecase.GetCategoriesUseCase
import com.ataglance.walletglance.category.presentation.model.CategoriesStatistics
import com.ataglance.walletglance.category.presentation.model.CategoriesStatisticsWidgetUiState
import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.transaction.domain.usecase.GetTransactionsInDateRangeUseCase
import com.ataglance.walletglance.transaction.domain.utils.filterNotEmptyByCategoryTypes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CategoryStatisticsWidgetViewModel(
    activeAccount: Account?,
    activeDateRange: TimestampRange,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getTransactionsInDateRangeUseCase: GetTransactionsInDateRangeUseCase
) : ViewModel() {

    private var groupedCategoriesByType: GroupedCategoriesByType? = null


    private val _activeAccount = MutableStateFlow(activeAccount)

    fun setActiveAccount(account: Account?) {
        if (_activeAccount.value == account) return

        _activeAccount.update { account }
    }


    private val _activeDateRange = MutableStateFlow(activeDateRange)

    fun setActiveDateRange(dateRange: TimestampRange) {
        if (_activeDateRange.value.equalsTo(dateRange)) return
        _activeDateRange.update { dateRange }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private val _transactionsInDateRange = _activeDateRange.flatMapLatest { dateRange ->
        getTransactionsInDateRangeUseCase.getAsFlowOrEmpty(range = dateRange)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    val statisticsUiState: StateFlow<CategoriesStatisticsWidgetUiState> = combine(
        _transactionsInDateRange,
        _activeAccount
    ) { transactions, account ->
        if (account == null) return@combine CategoriesStatisticsWidgetUiState()

        val groupedCategoriesByType = groupedCategoriesByType
            ?: getCategoriesUseCase.getGrouped().also { groupedCategoriesByType = it }

        val (categoryType, transactions) = transactions.filterNotEmptyByCategoryTypes()

        CategoriesStatistics
            .fromTransactions(
                type = categoryType,
                accountId = account.id,
                accountCurrency = account.currency,
                transactions = transactions,
                groupedCategories = groupedCategoriesByType.getByType(type = categoryType)
            )
            .let { CategoriesStatisticsWidgetUiState.fromStatistics(it.stats) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = CategoriesStatisticsWidgetUiState()
    )

}