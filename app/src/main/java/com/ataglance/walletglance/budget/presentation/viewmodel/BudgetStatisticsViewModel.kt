package com.ataglance.walletglance.budget.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.budget.domain.usecase.GetBudgetsUseCase
import com.ataglance.walletglance.budget.presentation.model.BudgetStatisticsScreenUiState
import com.ataglance.walletglance.core.domain.statistics.ColumnChartUiState
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.utils.getPrevDateRanges
import com.ataglance.walletglance.record.domain.usecase.GetRecordsTotalAmountInDateRangesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetStatisticsViewModel(
    budgetId: Int,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getBudgetsUseCase: GetBudgetsUseCase,
    private val getRecordsTotalAmountInDateRangesUseCase: GetRecordsTotalAmountInDateRangesUseCase,
    private val resourceManager: ResourceManager
) : ViewModel() {

    init {
        viewModelScope.launch {
            val accounts = getAccountsUseCase.getAll()
            val budget = getBudgetsUseCase.get(id = budgetId, accounts = accounts)
            budget?.category ?: return@launch

            getRecordsTotalAmountInDateRangesUseCase
                .getByCategoryAndAccountsFlow(
                    categoryId = budget.category.id,
                    accountsIds = budget.linkedAccountsIds,
                    dateRangeList = budget.repeatingPeriod.getPrevDateRanges()
                )
                .collect { totalInRanges ->
                    _uiState.update {
                        BudgetStatisticsScreenUiState(
                            budget = budget,
                            accounts = accounts,
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


    private val _uiState = MutableStateFlow(BudgetStatisticsScreenUiState())
    val uiState = _uiState.asStateFlow()

}