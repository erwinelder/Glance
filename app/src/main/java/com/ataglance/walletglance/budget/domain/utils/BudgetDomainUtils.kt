package com.ataglance.walletglance.budget.domain.utils

import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.transaction.domain.model.Transaction


fun List<Budget>.getMaxIdOrZero(): Int {
    return maxOfOrNull { it.id } ?: 0
}


fun List<Budget>.getWithMaxRepeatingPeriod(): Budget? {
    return find { it.repeatingPeriod == RepeatingPeriod.Yearly }
        ?: find { it.repeatingPeriod == RepeatingPeriod.Monthly }
        ?: find { it.repeatingPeriod == RepeatingPeriod.Weekly }
        ?: find { it.repeatingPeriod == RepeatingPeriod.Daily }
}

fun List<Budget>.getMaxDateRange(): TimestampRange? {
    return getWithMaxRepeatingPeriod()?.dateRange
}

fun List<Budget>.getFirstDateRange(): TimestampRange? {
    return firstOrNull()?.dateRange
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


fun List<Transaction>.filterByBudgetsDateRange(budgets: List<Budget>): List<Transaction> {
    val dateRange = budgets.firstOrNull()?.dateRange ?: return this
    return filter { dateRange.containsDate(date = it.date) }
}


fun List<Budget>.fillUsedAmountsByTransactions(transactions: List<Transaction>): List<Budget> {
    return map { budget ->
        budget.applyUsedAmount(
            amount = transactions.getTotalAmountCorrespondingToBudget(budget = budget)
        )
    }
}

fun List<Transaction>.getTotalAmountCorrespondingToBudget(budget: Budget): Double {
    if (budget.category == null || budget.linkedAccountIds.isEmpty()) return 0.0

    return sumOf { transaction ->
        transaction
            .takeIf { it.includeInBudgets }
            ?.getTotalExpensesByAccountsAndCategory(
                accountIds = budget.linkedAccountIds, categoryId = budget.category.id
            )
            ?: 0.0
    }
}
