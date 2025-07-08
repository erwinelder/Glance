package com.ataglance.walletglance.budget.data.local.model

data class BudgetEntityWithAssociations(
    val budget: BudgetEntity,
    val associations: List<BudgetAccountAssociationEntity>
) {

    val budgetId: Int
        get() = budget.id

    val deleted: Boolean
        get() = budget.deleted

    fun asPair(): Pair<BudgetEntity, List<BudgetAccountAssociationEntity>> {
        return budget to associations.takeUnless { budget.deleted }.orEmpty()
    }

}
