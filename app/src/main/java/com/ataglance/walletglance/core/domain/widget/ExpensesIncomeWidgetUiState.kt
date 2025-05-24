package com.ataglance.walletglance.core.domain.widget

import com.ataglance.walletglance.core.utils.formatWithSpaces
import com.ataglance.walletglance.record.domain.utils.getTotalPercentages

data class ExpensesIncomeWidgetUiState(
    val expensesTotal: Double = 0.0,
    val incomeTotal: Double = 0.0,
    val expensesPercentage: Double = 0.0,
    val incomePercentage: Double = 0.0,
    val expensesPercentageFloat: Float = 0.0f,
    val incomePercentageFloat: Float = 0.0f,
) {

    companion object {

        fun fromTotal(expensesTotal: Double, incomeTotal: Double): ExpensesIncomeWidgetUiState {
            val (expensesPercentage, incomePercentage) = getTotalPercentages(expensesTotal, incomeTotal)

            return ExpensesIncomeWidgetUiState(
                expensesTotal = expensesTotal,
                incomeTotal = incomeTotal,
                expensesPercentage = expensesPercentage,
                incomePercentage = incomePercentage,
                expensesPercentageFloat = expensesPercentage.toFloat() / 100F,
                incomePercentageFloat = incomePercentage.toFloat() / 100F
            )
        }

    }


    fun getTotalFormatted(): String = (incomeTotal - expensesTotal).formatWithSpaces()

    fun getExpensesTotalFormatted(): String = expensesTotal.formatWithSpaces()

    fun getIncomeTotalFormatted(): String = incomeTotal.formatWithSpaces()

}