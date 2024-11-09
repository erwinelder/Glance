package com.ataglance.walletglance.budget.data.utils

import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.model.BudgetEntity


fun List<BudgetEntity>.getBudgetsThatAreNotInList(list: List<BudgetEntity>): List<BudgetEntity> {
    return this.filter { budget ->
        list.find { it.id == budget.id } == null
    }
}

fun List<BudgetAccountAssociation>.getAssociationsThatAreNotInList(
    list: List<BudgetAccountAssociation>
): List<BudgetAccountAssociation> {
    return this.filter { thisAssociation ->
        list.find {
            it.budgetId == thisAssociation.budgetId &&
                    it.accountId == thisAssociation.accountId
        } == null
    }
}