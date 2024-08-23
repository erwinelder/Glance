package com.ataglance.walletglance.budget.utils

import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity


fun List<BudgetEntity>.getIdsThatAreNotInList(list: List<BudgetEntity>): List<Int> {
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