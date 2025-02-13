package com.ataglance.walletglance.budget.domain.utils

import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.record.domain.model.Record
import com.ataglance.walletglance.record.domain.utils.getTotalAmountCorrespondingToBudget


fun List<Budget>.findById(id: Int): Budget? {
    return this.find { it.id == id }
}


fun List<Budget>.getMaxIdOrZero(): Int {
    return this.maxOfOrNull { it.id } ?: 0
}


fun List<Budget>.getWithMaxRepeatingPeriod(): Budget? {
    return find { it.repeatingPeriod == RepeatingPeriod.Yearly }
        ?: find { it.repeatingPeriod == RepeatingPeriod.Monthly }
        ?: find { it.repeatingPeriod == RepeatingPeriod.Weekly }
        ?: find { it.repeatingPeriod == RepeatingPeriod.Daily }
}

fun List<Budget>.getMaxDateRange(): LongDateRange? {
    return getWithMaxRepeatingPeriod()?.dateRange
}

fun List<Budget>.getFirstDateRange(): LongDateRange? {
    return firstOrNull()?.dateRange
}


fun List<Budget>.fillUsedAmountsByRecords(recordList: List<Record>): List<Budget> {
    return this.map { budget ->
        budget.applyUsedAmount(recordList.getTotalAmountCorrespondingToBudget(budget))
    }
}


fun List<Budget>.groupByType(): BudgetsByType {
    val groupedBudgets = this
        .groupBy { it.repeatingPeriod }
        .mapValues { entry ->
            entry.value.sortedBy { it.priorityNum }
        }

    return BudgetsByType(
        daily = groupedBudgets[RepeatingPeriod.Daily].orEmpty(),
        weekly = groupedBudgets[RepeatingPeriod.Weekly].orEmpty(),
        monthly = groupedBudgets[RepeatingPeriod.Monthly].orEmpty(),
        yearly = groupedBudgets[RepeatingPeriod.Yearly].orEmpty()
    )
}
