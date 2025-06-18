package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod

class BudgetRepositoryMock : BudgetRepository {

    var budgets = listOf(
        BudgetEntity(
            id = 1,
            amountLimit = 100.0,
            categoryId = 1,
            name = "Budget name 1",
            repeatingPeriod = RepeatingPeriod.Monthly.name
        ),
        BudgetEntity(
            id = 2,
            amountLimit = 150.0,
            categoryId = 2,
            name = "Budget name 2",
            repeatingPeriod = RepeatingPeriod.Monthly.name
        ),
    )

    var associations = listOf(
        BudgetAccountAssociation(budgetId = 1, accountId = 1),
        BudgetAccountAssociation(budgetId = 2, accountId = 2)
    )


    override suspend fun deleteBudgetsAndAssociations(
        budgets: List<BudgetEntity>,
        associations: List<BudgetAccountAssociation>
    ) {}

    override suspend fun deleteAndUpsertBudgetsAndAssociations(
        budgetsToDelete: List<BudgetEntity>,
        budgetsToUpsert: List<BudgetEntity>,
        associationsToDelete: List<BudgetAccountAssociation>,
        associationsToUpsert: List<BudgetAccountAssociation>
    ) {}

    override suspend fun getBudgetAndAssociations(
        budgetId: Int
    ): Pair<BudgetEntity, List<BudgetAccountAssociation>>? {
        val budget = budgets.find { it.id == budgetId }
        val budgetAssociations = associations.filter { it.budgetId == budgetId }

        return budget?.let { it to budgetAssociations }
    }

    override suspend fun getAllBudgetsAndAssociations():
            Pair<List<BudgetEntity>, List<BudgetAccountAssociation>>
    {
        return budgets to associations
    }
}