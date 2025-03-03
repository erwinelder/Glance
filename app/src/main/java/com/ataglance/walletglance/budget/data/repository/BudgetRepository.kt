package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity

interface BudgetRepository {

    suspend fun deleteBudgetsAndAssociations(
        budgets: List<BudgetEntity>,
        associations: List<BudgetAccountAssociation>
    )

    suspend fun deleteAndUpsertBudgetsAndAssociations(
        budgetsToDelete: List<BudgetEntity>,
        budgetsToUpsert: List<BudgetEntity>,
        associationsToDelete: List<BudgetAccountAssociation>,
        associationsToUpsert: List<BudgetAccountAssociation>,
    )

    suspend fun getBudgetAndAssociations(budgetId: Int): Pair<BudgetEntity, List<BudgetAccountAssociation>>?

    suspend fun getAllBudgetsAndAssociations(): Pair<List<BudgetEntity>, List<BudgetAccountAssociation>>

}