package com.ataglance.walletglance.data.widgets

import com.ataglance.walletglance.data.categories.CategoryStatisticsLists
import com.ataglance.walletglance.data.records.RecordStack

data class WidgetsUiState(
    val recordsFilteredByDateAndAccount: List<RecordStack> = emptyList(),
    val greetings: GreetingsWidgetUiState = GreetingsWidgetUiState(),
    val expensesIncomeState: ExpensesIncomeWidgetUiState = ExpensesIncomeWidgetUiState(),
    val categoryStatisticsLists: CategoryStatisticsLists = CategoryStatisticsLists()
)