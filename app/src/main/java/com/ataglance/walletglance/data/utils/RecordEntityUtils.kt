package com.ataglance.walletglance.data.utils

import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.local.entities.RecordEntity

fun List<RecordEntity>.getIdsThatAreNotInList(list: List<RecordEntity>): List<Int> {
    return this
        .mapNotNull { record ->
            record.id.takeIf { id ->
                list.find { it.id == id } == null
            }
        }
}

fun List<RecordEntity>.getTotalAmountByType(type: CategoryType): Double {
    return this
        .filter {
            type == CategoryType.Expense && it.isExpenseOrOutTransfer() ||
                    type == CategoryType.Income && it.isIncomeOrInTransfer()
        }
        .fold(0.0) { total, record ->
            total + record.amount
        }
}

fun List<RecordEntity>.getTotalAmountCorrespondingToBudget(budget: Budget): Double {
    return this
        .filter {
            it.containsParentOrSubcategoryId(budget.category?.id) &&
                    budget.containsAccountId(it.accountId)
        }
        .fold(0.0) { total, record ->
            total + record.amount
        }
}
