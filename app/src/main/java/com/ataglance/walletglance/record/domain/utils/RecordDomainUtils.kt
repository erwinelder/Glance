package com.ataglance.walletglance.record.domain.utils

import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.core.utils.extractYear
import com.ataglance.walletglance.record.domain.model.Record
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.model.RecordStackItem
import com.ataglance.walletglance.record.domain.model.RecordType


fun RecordType.inverse(): RecordType {
    return when (this) {
        RecordType.Expense -> RecordType.Income
        RecordType.Income -> RecordType.Expense
        RecordType.OutTransfer -> RecordType.InTransfer
        RecordType.InTransfer -> RecordType.OutTransfer
    }
}

fun RecordType.toCategoryTypeOrNullIfTransfer(): CategoryType? {
    return when (this) {
        RecordType.Expense -> CategoryType.Expense
        RecordType.Income -> CategoryType.Income
        else -> null
    }
}

fun RecordType.asChar(): Char {
    return when (this) {
        RecordType.Expense -> '-'
        RecordType.Income -> '+'
        RecordType.OutTransfer -> '>'
        RecordType.InTransfer -> '<'
    }
}

fun Char.asRecordType(): RecordType? {
    return when (this) {
        '-' -> RecordType.Expense
        '+' -> RecordType.Income
        '>' -> RecordType.OutTransfer
        '<' -> RecordType.InTransfer
        else -> null
    }
}


fun List<RecordStack>.containsRecordsFromDifferentYears(): Boolean {
    return this.isNotEmpty() &&
            this.first().date.extractYear() != this.last().date.extractYear()
}


fun List<RecordStack>.filterByAccount(accountId: Int?): List<RecordStack> {
    if (accountId == null) return this
    return filter { it.account.id == accountId }
}


fun List<RecordStack>.filterByCollectionType(type: CategoryCollectionType): List<RecordStack> {
    return when (type) {
        CategoryCollectionType.Mixed -> this
        CategoryCollectionType.Expense -> this.filter { it.isExpenseOrOutTransfer() }
        CategoryCollectionType.Income -> this.filter { it.isIncomeOrInTransfer() }
    }
}


fun List<RecordStack>.filterByCollection(collection: CategoryCollectionWithIds): List<RecordStack> {
    return filterByCollectionType(collection.type).let { recordStacks ->
        if (collection.categoriesIds.isNullOrEmpty()) {
            recordStacks
        } else {
            recordStacks.filterByCategoriesIds(collection.categoriesIds)
        }
    }
}


fun List<RecordStack>.filterByCategoriesIds(categoriesIds: List<Int>): List<RecordStack> {
    return this.mapNotNull { recordStack ->
        recordStack.stack
            .filter { it.categoryWithSub?.matchIds(categoriesIds) == true }
            .takeIf { it.isNotEmpty() }
            ?.let { recordStack.copy(stack = it) }
    }
}


fun List<RecordStack>.shrinkForCompactView(): List<RecordStack> {
    return map {
        if (it.isExpenseOrIncome()) it.shrinkToCompactView() else it
    }
}

fun List<RecordStackItem>.distinctByCategories(maxCount: Int): List<RecordStackItem> {
    val distinctItems = mutableListOf<RecordStackItem>()

    for (i in indices) {
        if (distinctItems.size >= maxCount) { break }

        this[i]
            .takeIf { item ->
                distinctItems.none { it.matchesCategory(item.categoryWithSub) }
            }
            ?.let { distinctItems.add(it) }
    }

    return distinctItems
}


fun List<RecordStack>.getTotalAmountByType(type: CategoryType): Double {
    val typeChecker = when (type) {
        CategoryType.Expense -> RecordStack::isExpenseOrOutTransfer
        CategoryType.Income -> RecordStack::isIncomeOrInTransfer
    }

    return this
        .filter { typeChecker(it) }
        .fold(0.0) { total, recordStack ->
            total + recordStack.totalAmount
        }
}

fun getTotalPercentages(expensesTotal: Double, incomeTotal: Double): Pair<Double, Double> {
    return (expensesTotal + incomeTotal)
        .takeUnless { it == 0.0 }
        ?.let { (100 / it) * expensesTotal to (100 / it) * incomeTotal }
        ?: (0.0 to 0.0)
}


fun getStartAndFinalRateByAmounts(
    startAmount: Double,
    finalAmount: Double
): Pair<Double, Double> {
    return if (startAmount <= finalAmount) {
        1.0 to finalAmount / startAmount
    } else {
        startAmount / finalAmount to 1.0
    }
}


fun List<Record>.filterByBudgetsDateRange(budgets: List<Budget>): List<Record> {
    val dateRange = budgets.firstOrNull()?.dateRange ?: return this
    return this.filter { dateRange.containsDate(it.date) }
}

fun List<Record>.getTotalAmountCorrespondingToBudget(budget: Budget): Double {
    return this
        .filter {
            it.includeInBudgets &&
                    it.containsParentOrSubcategoryId(budget.category?.id) &&
                    budget.containsAccount(it.accountId)
        }
        .fold(0.0) { total, record ->
            total + record.amount
        }
}
