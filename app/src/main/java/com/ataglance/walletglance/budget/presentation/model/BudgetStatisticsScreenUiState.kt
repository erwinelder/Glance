package com.ataglance.walletglance.budget.presentation.model

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.core.domain.statistics.ColumnChartUiState

data class BudgetStatisticsScreenUiState(
    val budget: Budget? = null,
    val accounts: List<Account> = emptyList(),
    val columnChartUiState: ColumnChartUiState = ColumnChartUiState()
)
