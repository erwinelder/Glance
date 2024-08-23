package com.ataglance.walletglance.domain.widgets

import com.ataglance.walletglance.domain.categories.CategoryStatisticsLists
import com.ataglance.walletglance.domain.records.RecordStack

data class WidgetsUiState(
    val recordsFilteredByDateAndAccount: List<RecordStack> = emptyList(),
    val greetings: GreetingsWidgetUiState = GreetingsWidgetUiState(),
    val expensesIncomeState: ExpensesIncomeWidgetUiState = ExpensesIncomeWidgetUiState(),
    val categoryStatisticsLists: CategoryStatisticsLists = CategoryStatisticsLists()
)