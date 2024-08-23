package com.ataglance.walletglance.core.domain.widgets

import com.ataglance.walletglance.core.utils.formatWithSpaces

data class ExpensesIncomeWidgetUiState(
    val expensesTotal: Double = 0.0,
    val incomeTotal: Double = 0.0,
    val expensesPercentage: Double = 0.0,
    val incomePercentage: Double = 0.0,
    val expensesPercentageFloat: Float = 0.0f,
    val incomePercentageFloat: Float = 0.0f,
) {
    fun getTotalFormatted(): String = (incomeTotal - expensesTotal).formatWithSpaces()
    fun getExpensesTotalFormatted(): String = expensesTotal.formatWithSpaces()
    fun getIncomeTotalFormatted(): String = incomeTotal.formatWithSpaces()
}