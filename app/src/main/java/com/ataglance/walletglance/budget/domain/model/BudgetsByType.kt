package com.ataglance.walletglance.budget.domain.model

import com.ataglance.walletglance.budget.domain.utils.fillUsedAmountsByTransactions
import com.ataglance.walletglance.budget.domain.utils.filterByBudgetsDateRange
import com.ataglance.walletglance.budget.domain.utils.getFirstDateRange
import com.ataglance.walletglance.budget.domain.utils.getMaxIdOrZero
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.transaction.domain.model.Transaction

data class BudgetsByType(
    val daily: List<Budget> = emptyList(),
    val weekly: List<Budget> = emptyList(),
    val monthly: List<Budget> = emptyList(),
    val yearly: List<Budget> = emptyList()
) {

    fun areEmpty(): Boolean {
        return daily.isEmpty() && weekly.isEmpty() && monthly.isEmpty() && yearly.isEmpty()
    }

    fun getMaxDateRange(): TimestampRange? {
        return yearly.getFirstDateRange()
            ?: monthly.getFirstDateRange()
            ?: weekly.getFirstDateRange()
            ?: daily.getFirstDateRange()
    }

    fun concatenate(): List<Budget> {
        return daily + weekly + monthly + yearly
    }

    fun getByType(type: RepeatingPeriod): List<Budget> {
        return when (type) {
            RepeatingPeriod.Daily -> daily
            RepeatingPeriod.Weekly -> weekly
            RepeatingPeriod.Monthly -> monthly
            RepeatingPeriod.Yearly -> yearly
        }
    }

    fun iterateByType(action: (RepeatingPeriod, List<Budget>) -> Unit) {
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


    private fun replaceListByType(list: List<Budget>, type: RepeatingPeriod): BudgetsByType {
        return when (type) {
            RepeatingPeriod.Daily -> this.copy(daily = list)
            RepeatingPeriod.Weekly -> this.copy(weekly = list)
            RepeatingPeriod.Monthly -> this.copy(monthly = list)
            RepeatingPeriod.Yearly -> this.copy(yearly = list)
        }
    }

    fun addBudget(budget: Budget): BudgetsByType {
        val newId = concatenate().getMaxIdOrZero() + 1

        val newList = (getByType(budget.repeatingPeriod) + listOf(budget.copy(id = newId)))
            .sortedBy { it.priorityNum }

        return replaceListByType(newList, budget.repeatingPeriod)
    }

    fun deleteBudget(id: Int, repeatingPeriod: RepeatingPeriod): BudgetsByType {
        return when (repeatingPeriod) {
            RepeatingPeriod.Daily -> this.copy(daily = daily.filter { it.id != id })
            RepeatingPeriod.Weekly -> this.copy(weekly = weekly.filter { it.id != id })
            RepeatingPeriod.Monthly -> this.copy(monthly = monthly.filter { it.id != id })
            RepeatingPeriod.Yearly -> this.copy(yearly = yearly.filter { it.id != id })
        }
    }


    fun fillUsedAmountsByTransactions(transactions: List<Transaction>): BudgetsByType {
        var transactions = transactions.filterByBudgetsDateRange(yearly)
        val filledYearlyBudgets = yearly.fillUsedAmountsByTransactions(transactions)

        transactions = transactions.filterByBudgetsDateRange(monthly)
        val filledMonthlyBudgets = monthly.fillUsedAmountsByTransactions(transactions)

        transactions = transactions.filterByBudgetsDateRange(weekly)
        val filledWeeklyBudgets = weekly.fillUsedAmountsByTransactions(transactions)

        transactions = transactions.filterByBudgetsDateRange(daily)
        val filledDailyBudgets = daily.fillUsedAmountsByTransactions(transactions)

        return copy(
            daily = filledDailyBudgets,
            weekly = filledWeeklyBudgets,
            monthly = filledMonthlyBudgets,
            yearly = filledYearlyBudgets
        )
    }

}
