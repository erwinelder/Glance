package com.ataglance.walletglance.budget.domain.model

import com.ataglance.walletglance.core.domain.date.RepeatingPeriod

data class CheckedBudgetsByType(
    val daily: List<CheckedBudget> = emptyList(),
    val weekly: List<CheckedBudget> = emptyList(),
    val monthly: List<CheckedBudget> = emptyList(),
    val yearly: List<CheckedBudget> = emptyList()
) {

    private fun concatenate(): List<CheckedBudget> {
        return daily + weekly + monthly + yearly
    }

    fun countCheckedBudgets(): Int {
        return concatenate().count { it.checked }
    }

    fun iterateByType(action: (RepeatingPeriod, List<CheckedBudget>) -> Unit) {
        if (daily.isNotEmpty()) {
            action(RepeatingPeriod.Daily, daily)
        }
        if (weekly.isNotEmpty()) {
            action(RepeatingPeriod.Weekly, weekly)
        }
        if (monthly.isNotEmpty()) {
            action(RepeatingPeriod.Monthly, monthly)
        }
        if (yearly.isNotEmpty()) {
            action(RepeatingPeriod.Yearly, yearly)
        }
    }

}
