package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.utils.filterByBudgetAccounts
import com.ataglance.walletglance.budget.domain.usecase.GetEmptyBudgetsUseCase
import com.ataglance.walletglance.budget.presentation.model.BudgetStatisticsScreenUiState
import com.ataglance.walletglance.core.domain.statistics.ColumnChartUiState
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.utils.getPrevDateRanges
import com.ataglance.walletglance.transaction.domain.usecase.GetTotalExpensesInDateRangesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetStatisticsViewModel(
    budgetId: Int,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getEmptyBudgetsUseCase: GetEmptyBudgetsUseCase,
    private val getTotalExpensesInDateRangesUseCase: GetTotalExpensesInDateRangesUseCase,
    private val resourceManager: ResourceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetStatisticsScreenUiState())
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            val accounts = getAccountsUseCase.getAll()
            val budget = getEmptyBudgetsUseCase.get(id = budgetId, accounts = accounts)
            budget?.category ?: return@launch

            getTotalExpensesInDateRangesUseCase
                .getByCategoryAndAccounts(
                    categoryId = budget.category.id,
                    accountIds = budget.linkedAccountIds,
                    dateRanges = budget.repeatingPeriod.getPrevDateRanges()
                )
                .collect { totalInRanges ->
                    _uiState.update {
                        BudgetStatisticsScreenUiState(
                            budget = budget,
                            accounts = accounts.filterByBudgetAccounts(budget),
                            columnChartUiState = ColumnChartUiState.asAmountsByDateRanges(
                                totalAmountsByRanges = totalInRanges,
                                rowsCount = 5,
                                repeatingPeriod = budget.repeatingPeriod,
                                resourceManager = resourceManager
                            )
                        )
                    }
                }
        }
    }

}