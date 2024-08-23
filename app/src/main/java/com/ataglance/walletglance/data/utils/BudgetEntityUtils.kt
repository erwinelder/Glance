package com.ataglance.walletglance.data.utils

import com.ataglance.walletglance.data.local.entities.BudgetAccountAssociation
import com.ataglance.walletglance.data.local.entities.BudgetEntity


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