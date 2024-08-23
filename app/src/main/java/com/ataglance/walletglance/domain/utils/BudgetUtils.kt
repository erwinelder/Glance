package com.ataglance.walletglance.domain.utils

import com.ataglance.walletglance.domain.budgets.Budget
import com.ataglance.walletglance.domain.budgets.BudgetsByType
import com.ataglance.walletglance.domain.budgets.EditingBudgetUiState
import com.ataglance.walletglance.domain.date.RepeatingPeriod
import com.ataglance.walletglance.data.local.entities.RecordEntity
import com.ataglance.walletglance.data.utils.getTotalAmountCorrespondingToBudget


fun List<Budget>.groupByType(): BudgetsByType {
    var dailyBudgets: List<Budget>
    var weeklyBudgets: List<Budget>
    var monthlyBudgets: List<Budget>
    var otherBudgets: List<Budget>

    this.partition { it.repeatingPeriod == RepeatingPeriod.Daily }.let {
        dailyBudgets = it.first
        otherBudgets = it.second
    }
    otherBudgets.partition { it.repeatingPeriod == RepeatingPeriod.Weekly }.let {
        weeklyBudgets = it.first
        otherBudgets = it.second
    }
    otherBudgets.partition { it.repeatingPeriod == RepeatingPeriod.Monthly }.let {
        monthlyBudgets = it.first
        otherBudgets = it.second
    }
    val yearlyBudgets = otherBudgets.partition { it.repeatingPeriod == RepeatingPeriod.Yearly }
        .first

    return BudgetsByType(
        daily = dailyBudgets.sortedBy { it.priorityNum },
        weekly = weeklyBudgets.sortedBy { it.priorityNum },
        monthly = monthlyBudgets.sortedBy { it.priorityNum },
        yearly = yearlyBudgets.sortedBy { it.priorityNum }
    )
}


fun List<Budget>.findById(id: Int): Budget? {
    return this.find { it.id == id }
}


fun List<Budget>.getMaxIdOrZero(): Int {
    return this.maxOfOrNull { it.id } ?: 0
}


fun List<Budget>.replaceById(editingBudgetUiState: EditingBudgetUiState): List<Budget> {
    return this.map { budget ->
        budget.takeUnless { it.id == editingBudgetUiState.id }
            ?: editingBudgetUiState.copyDataToBudget(budget)
    }
}


fun List<Budget>.fillUsedAmountsByRecords(recordList: List<RecordEntity>): List<Budget> {
    return this.map { budget ->
        budget.applyUsedAmount(recordList.getTotalAmountCorrespondingToBudget(budget))
    }
}


fun List<Budget>.addUsedAmountsByRecords(recordList: List<RecordEntity>): List<Budget> {
    if (recordList.isEmpty()) return this
    return this.map { budget ->
        budget.addToUsedAmount(recordList.getTotalAmountCorrespondingToBudget(budget))
    }
}


fun List<Budget>.subtractUsedAmountsByRecords(recordList: List<RecordEntity>): List<Budget> {
    if (recordList.isEmpty()) return this
    return this.map { budget ->
        budget.subtractFromUsedAmount(recordList.getTotalAmountCorrespondingToBudget(budget))
    }
}
