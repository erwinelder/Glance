package com.ataglance.walletglance.data.budgets

import com.ataglance.walletglance.data.date.LongDateRange
import com.ataglance.walletglance.data.date.RepeatingPeriod
import com.ataglance.walletglance.data.utils.addUsedAmountsByRecords
import com.ataglance.walletglance.data.utils.fillUsedAmountsByRecords
import com.ataglance.walletglance.data.utils.getMaxIdOrZero
import com.ataglance.walletglance.data.utils.subtractUsedAmountsByRecords
import com.ataglance.walletglance.domain.entities.Record

data class BudgetsByType(
    val daily: List<Budget> = emptyList(),
    val weekly: List<Budget> = emptyList(),
    val monthly: List<Budget> = emptyList(),
    val yearly: List<Budget> = emptyList()
) {

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

    fun addBudget(budget: Budget): BudgetsByType {
        val newId = concatenate().getMaxIdOrZero() + 1
        val listOfNewBudget = listOf(budget.copy(id = newId))

        return when (budget.repeatingPeriod) {
            RepeatingPeriod.Daily -> this.copy(daily = daily + listOfNewBudget)
            RepeatingPeriod.Weekly -> this.copy(daily = weekly + listOfNewBudget)
            RepeatingPeriod.Monthly -> this.copy(daily = monthly + listOfNewBudget)
            RepeatingPeriod.Yearly -> this.copy(daily = yearly + listOfNewBudget)
        }
    }

    fun getMaxDateRange(): LongDateRange? {
        return (yearly.takeIf { it.isNotEmpty() }
            ?: monthly.takeIf { it.isNotEmpty() }
            ?: weekly.takeIf { it.isNotEmpty() }
            ?: daily.takeIf { it.isNotEmpty() })
                ?.first()?.dateRange
    }

    fun fillUsedAmountsByRecords(recordList: List<Record>): BudgetsByType {
        var recordsInDateRange = recordList.filterByBudgetsDateRange(yearly)
        val filledYearlyBudgets = recordsInDateRange?.let { yearly.fillUsedAmountsByRecords(it) }

        recordsInDateRange = recordsInDateRange?.filterByBudgetsDateRange(monthly)
        val filledMonthlyBudgets = recordsInDateRange?.let { monthly.fillUsedAmountsByRecords(it) }

        recordsInDateRange = recordsInDateRange?.filterByBudgetsDateRange(weekly)
        val filledWeeklyBudgets = recordsInDateRange?.let { weekly.fillUsedAmountsByRecords(it) }

        recordsInDateRange = recordsInDateRange?.filterByBudgetsDateRange(daily)
        val filledDailyBudgets = recordsInDateRange?.let { daily.fillUsedAmountsByRecords(it) }

        return this.copy(
            daily = filledDailyBudgets ?: emptyList(),
            weekly = filledWeeklyBudgets ?: emptyList(),
            monthly = filledMonthlyBudgets ?: emptyList(),
            yearly = filledYearlyBudgets ?: emptyList()
        )
    }

    fun addUsedAmountsByRecords(recordList: List<Record>): BudgetsByType {
        if (recordList.isEmpty()) return this

        return applyRecordAmountsToBudgets(
            recordList = recordList,
            applyFunction = List<Budget>::addUsedAmountsByRecords
        )
    }

    fun subtractUsedAmountsByRecords(recordList: List<Record>): BudgetsByType {
        if (recordList.isEmpty()) return this

        return applyRecordAmountsToBudgets(
            recordList = recordList,
            applyFunction = List<Budget>::subtractUsedAmountsByRecords
        )
    }

    private fun applyRecordAmountsToBudgets(
        recordList: List<Record>,
        applyFunction: List<Budget>.(List<Record>) -> List<Budget>
    ): BudgetsByType {
        var recordsInDateRange = recordList.filterByBudgetsDateRange(yearly)
            ?.takeIf { it.isNotEmpty() }
        val filledYearlyBudgets = recordsInDateRange?.let { yearly.applyFunction(it) }

        recordsInDateRange = recordsInDateRange?.filterByBudgetsDateRange(monthly)
            ?.takeIf { it.isNotEmpty() }
        val filledMonthlyBudgets = recordsInDateRange?.let { monthly.applyFunction(it) }

        recordsInDateRange = recordsInDateRange?.filterByBudgetsDateRange(weekly)
            ?.takeIf { it.isNotEmpty() }
        val filledWeeklyBudgets = recordsInDateRange?.let { weekly.applyFunction(it) }

        recordsInDateRange = recordsInDateRange?.filterByBudgetsDateRange(daily)
            ?.takeIf { it.isNotEmpty() }
        val filledDailyBudgets = recordsInDateRange?.let { daily.applyFunction(it) }

        return this.copy(
            daily = filledDailyBudgets ?: daily,
            weekly = filledWeeklyBudgets ?: weekly,
            monthly = filledMonthlyBudgets ?: monthly,
            yearly = filledYearlyBudgets ?: yearly
        )
    }

    private fun List<Record>.filterByBudgetsDateRange(budgetList: List<Budget>): List<Record>? {
        return budgetList.firstOrNull()?.dateRange?.let { dateRange ->
            this.filter { dateRange.containsDate(it.date) }
        }
    }

}
