package com.ataglance.walletglance.core.domain.widgets

import com.ataglance.walletglance.category.domain.CategoryStatisticsLists
import com.ataglance.walletglance.record.domain.RecordStack

data class WidgetsUiState(
    val recordsFilteredByDateAndAccount: List<RecordStack> = emptyList(),
    val greetings: GreetingsWidgetUiState = GreetingsWidgetUiState(),
    val expensesIncomeState: ExpensesIncomeWidgetUiState = ExpensesIncomeWidgetUiState(),
    val categoryStatisticsLists: CategoryStatisticsLists = CategoryStatisticsLists()
)