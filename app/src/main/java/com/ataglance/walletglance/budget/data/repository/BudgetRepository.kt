package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity

interface BudgetRepository {

    suspend fun deleteAndUpsertBudgetsAndAssociations(
        budgetsToDelete: List<BudgetEntity>,
        budgetsToUpsert: List<BudgetEntity>,
        associationsToDelete: List<BudgetAccountAssociation>,
        associationsToUpsert: List<BudgetAccountAssociation>,
    )

    suspend fun getAllBudgetsAndAssociations(): Pair<List<BudgetEntity>, List<BudgetAccountAssociation>>

}