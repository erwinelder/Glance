package com.ataglance.walletglance.record.utils

import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.record.data.local.model.RecordEntity

fun List<RecordEntity>.getIdsThatAreNotInList(list: List<RecordEntity>): List<Int> {
    return this
        .mapNotNull { record ->
            record.id.takeIf { id ->
                list.find { it.id == id } == null
            }
        }
}

fun List<RecordEntity>.filterByAccountId(accountId: Int): List<RecordEntity> {
    return this.filter { it.accountId == accountId }
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
            it.includeInBudgets &&
                    it.containsParentOrSubcategoryId(budget.category?.id) &&
                    budget.containsAccountId(it.accountId)
        }
        .fold(0.0) { total, record ->
            total + record.amount
        }
}
