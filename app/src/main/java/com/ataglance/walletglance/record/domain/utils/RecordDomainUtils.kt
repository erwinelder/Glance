package com.ataglance.walletglance.record.domain.utils

import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategory
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


fun List<RecordStack>.filterAccountId(accountId: Int): List<RecordStack> {
    return this.filter {
        it.account.id == accountId
    }
}


fun List<RecordStack>.filterByCollectionType(type: CategoryCollectionType): List<RecordStack> {
    return when (type) {
        CategoryCollectionType.Mixed -> this
        CategoryCollectionType.Expense -> this.filter { it.isExpenseOrOutTransfer() }
        CategoryCollectionType.Income -> this.filter { it.isIncomeOrInTransfer() }
    }
}


fun List<RecordStack>.filterByCollection(collection: CategoryCollectionWithIds): List<RecordStack> {
    return this.filterByCollectionType(collection.type).let { recordStacksFilteredByType ->
        recordStacksFilteredByType.takeUnless { collection.hasLinkedCategories() }
            ?: recordStacksFilteredByType.filterByCategoriesIds(collection.categoriesIds!!)
    }
}


fun List<RecordStack>.filterByCategoriesIds(categoriesIds: List<Int>): List<RecordStack> {
    return this.mapNotNull { recordStack ->
        recordStack.stack
            .filter { it.categoryWithSubcategory?.matchIds(categoriesIds) == true }
            .takeIf { it.isNotEmpty() }
            ?.let { recordStack.copy(stack = it) }
    }
}


fun List<RecordStack>.getFirstByTypeAndAccountIdOrJustType(
    type: CategoryType,
    accountId: Int
): RecordStack? {
    return find { it.isExplicitlyOfType(type) && it.account.id == accountId }
        ?: find { it.isExplicitlyOfType(type) }
}


fun List<RecordStack>.shrinkForCompactView(): List<RecordStack> {
    return this.map {
        if (it.isExpenseOrIncome()) it.shrinkForCompactView() else it
    }
}

fun RecordStack.shrinkForCompactView(): RecordStack {
    val distinctStack = this.stack.distinctByCategories(3)

    val stack = distinctStack.map { item ->
        item.copy(note = item.categoryWithSubcategory?.let { this.stack.foldNotesByCategory(it) })
    }

    return this.copy(stack = stack)
}

fun List<RecordStackItem>.distinctByCategories(maxCount: Int): List<RecordStackItem> {
    val distinctItems = mutableListOf<RecordStackItem>()

    for (i in this.indices) {
        if (distinctItems.size >= maxCount) { break }

        this[i]
            .takeIf { item ->
                distinctItems.none { it.matchesCategory(item.categoryWithSubcategory) }
            }
            ?.let { distinctItems.add(it) }
    }

    return distinctItems
}

fun List<RecordStackItem>.foldNotesByCategory(
    categoryWithSubcategory: CategoryWithSubcategory
): String? {
    return this
        .filter {
            it.categoryWithSubcategory?.match(categoryWithSubcategory) == true &&
                    it.note?.isNotBlank() == true
        }
        .fold("") { notes, item ->
            notes + if (notes.isNotBlank()) { ", " } else { "" } + item.note
        }
        .takeIf { it.isNotBlank() }
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
                    budget.containsAccountId(it.accountId)
        }
        .fold(0.0) { total, record ->
            total + record.amount
        }
}
