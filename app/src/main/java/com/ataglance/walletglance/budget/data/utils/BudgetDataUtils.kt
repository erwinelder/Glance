package com.ataglance.walletglance.budget.data.utils

import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociationEntity
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.data.local.model.BudgetEntityWithAssociations


fun List<BudgetEntityWithAssociations>.divide(
): Pair<List<BudgetEntity>, List<BudgetAccountAssociationEntity>> {
    return map { it.asPair() }
        .unzip()
        .let { it.first to it.second.flatten() }
}

fun List<BudgetEntity>.zipWithAssociations(
    associations: List<BudgetAccountAssociationEntity>
): List<BudgetEntityWithAssociations> {
    return associateWith { budget ->
            if (!budget.deleted) associations.filter { it.budgetId == budget.id } else emptyList()
        }
        .map { (budget, associations) ->
            BudgetEntityWithAssociations(budget = budget, associations = associations)
        }
}
