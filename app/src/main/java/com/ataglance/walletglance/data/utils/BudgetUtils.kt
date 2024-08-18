package com.ataglance.walletglance.data.utils

import android.content.Context
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.budgets.BudgetsByType
import com.ataglance.walletglance.data.budgets.EditingBudgetUiState
import com.ataglance.walletglance.data.budgets.TotalAmountByRange
import com.ataglance.walletglance.data.categories.CategoryWithSubcategories
import com.ataglance.walletglance.data.date.RepeatingPeriod
import com.ataglance.walletglance.data.statistics.ColumnChartUiState
import com.ataglance.walletglance.domain.entities.BudgetAccountAssociation
import com.ataglance.walletglance.domain.entities.BudgetEntity
import com.ataglance.walletglance.domain.entities.Record


fun List<BudgetEntity>.toBudgetList(
    categoryWithSubcategoriesList: List<CategoryWithSubcategories>,
    associationList: List<BudgetAccountAssociation>,
    accountList: List<Account>
): List<Budget> {
    return this.mapNotNull { budgetEntity ->
        budgetEntity.toBudget(
            categoryWithSubcategory = categoryWithSubcategoriesList
                .getCategoryWithSubcategoryById(budgetEntity.categoryId),
            linkedAccountsIds = associationList
                .filter { it.budgetId == budgetEntity.id }
                .map { it.accountId },
            accountList = accountList
        )
    }
}


fun List<Budget>.toEntityList(): List<BudgetEntity> {
    return this.map { it.toEntity() }
}


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


fun List<Budget>.divideIntoBudgetsAndAssociations():
        Pair<List<BudgetEntity>, List<BudgetAccountAssociation>>
{
    val budgetList = this.toEntityList()
    val associationList = this.flatMap { budget ->
        budget.linkedAccountsIds.map { accountId ->
            BudgetAccountAssociation(
                budgetId = budget.id,
                accountId = accountId
            )
        }
    }
    return budgetList to associationList
}


fun List<BudgetEntity>.getIdsThatAreNotInList(
    list: List<BudgetEntity>
): List<Int> {
    return this
        .filter { budget ->
            list.find { it.id == budget.id } == null
        }
        .map { it.id }
}


fun List<BudgetAccountAssociation>.getAssociationsThatAreNotInList(
    list: List<BudgetAccountAssociation>
): List<BudgetAccountAssociation> {
    return this
        .filter { thisAssociation ->
            list.find {
                it.budgetId == thisAssociation.budgetId &&
                        it.accountId == thisAssociation.accountId
            } == null
        }
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


fun List<Budget>.fillUsedAmountsByRecords(recordList: List<Record>): List<Budget> {
    return this.map { budget ->
        budget.applyUsedAmount(recordList.getTotalAmountCorrespondingToBudget(budget))
    }
}


fun List<Budget>.addUsedAmountsByRecords(recordList: List<Record>): List<Budget> {
    if (recordList.isEmpty()) return this
    return this.map { budget ->
        budget.addToUsedAmount(recordList.getTotalAmountCorrespondingToBudget(budget))
    }
}


fun List<Budget>.subtractUsedAmountsByRecords(recordList: List<Record>): List<Budget> {
    if (recordList.isEmpty()) return this
    return this.map { budget ->
        budget.subtractFromUsedAmount(recordList.getTotalAmountCorrespondingToBudget(budget))
    }
}


fun List<TotalAmountByRange>.toColumnChartUiState(
    horizontalLinesCount: Int,
    repeatingPeriod: RepeatingPeriod,
    context: Context
): ColumnChartUiState {
    val maxAmount = this.maxOfOrNull { it.totalAmount } ?: 0.0

    val columnChartItemUiStateList = this.map { amountByRange ->
        amountByRange.toColumnChartItemUiState(
            maxTotalAmount = maxAmount,
            repeatingPeriod = repeatingPeriod,
            context = context
        )
    }

    return ColumnChartUiState(
        columns = columnChartItemUiStateList,
        horizontalLinesNames = maxAmount.getColumnChartHorizontalLinesNames(horizontalLinesCount)
    )
}
