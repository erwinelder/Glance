package com.ataglance.walletglance.core.domain.widgets

import androidx.annotation.StringRes
import com.ataglance.walletglance.category.presentation.model.CategoryStatisticsLists
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.record.domain.model.RecordStack

data class WidgetsUiState(
    @StringRes val greetingsTitleRes: Int,
    val activeAccountExpensesForToday: Double = 0.0,
    val recordStacksByDateAndAccount: List<RecordStack> = emptyList(),

    val widgetNamesList: List<WidgetName> = emptyList(),
    val expensesIncomeWidgetUiState: ExpensesIncomeWidgetUiState = ExpensesIncomeWidgetUiState(),
    val compactRecordStacksByDateAndAccount: List<RecordStack> = emptyList(),
    val categoryStatisticsLists: CategoryStatisticsLists = CategoryStatisticsLists()
)