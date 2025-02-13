package com.ataglance.walletglance.budget.domain.model

import com.ataglance.walletglance.budget.domain.utils.fillUsedAmountsByRecords
import com.ataglance.walletglance.budget.domain.utils.findById
import com.ataglance.walletglance.budget.domain.utils.getFirstDateRange
import com.ataglance.walletglance.budget.domain.utils.getMaxIdOrZero
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.record.domain.model.Record
import com.ataglance.walletglance.record.domain.utils.filterByBudgetsDateRange

data class BudgetsByType(
    val daily: List<Budget> = emptyList(),
    val weekly: List<Budget> = emptyList(),
    val monthly: List<Budget> = emptyList(),
    val yearly: List<Budget> = emptyList()
) {

    fun areEmpty(): Boolean {
        return daily.isEmpty() && weekly.isEmpty() && monthly.isEmpty() && yearly.isEmpty()
    }

    fun findById(id: Int): Budget? {
        return daily.findById(id)
            ?: weekly.findById(id)
            ?: monthly.findById(id)
            ?: yearly.findById(id)
    }

    fun getMaxDateRange(): LongDateRange? {
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


    fun fillUsedAmountsByRecords(records: List<Record>): BudgetsByType {
        var recordsInDateRange = records.filterByBudgetsDateRange(yearly)
        val filledYearlyBudgets = recordsInDateRange.let { yearly.fillUsedAmountsByRecords(it) }

        recordsInDateRange = recordsInDateRange.filterByBudgetsDateRange(monthly)
        val filledMonthlyBudgets = recordsInDateRange.let { monthly.fillUsedAmountsByRecords(it) }

        recordsInDateRange = recordsInDateRange.filterByBudgetsDateRange(weekly)
        val filledWeeklyBudgets = recordsInDateRange.let { weekly.fillUsedAmountsByRecords(it) }

        recordsInDateRange = recordsInDateRange.filterByBudgetsDateRange(daily)
        val filledDailyBudgets = recordsInDateRange.let { daily.fillUsedAmountsByRecords(it) }

        return this.copy(
            daily = filledDailyBudgets,
            weekly = filledWeeklyBudgets,
            monthly = filledMonthlyBudgets,
            yearly = filledYearlyBudgets
        )
    }

}
