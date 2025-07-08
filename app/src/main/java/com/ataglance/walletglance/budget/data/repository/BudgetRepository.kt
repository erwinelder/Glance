package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.data.model.BudgetDataModel
import com.ataglance.walletglance.budget.data.model.BudgetDataModelWithAssociations

interface BudgetRepository {

    suspend fun deleteAndUpsertBudgetsWithAssociations(
        toDelete: List<BudgetDataModel>,
        toUpsert: List<BudgetDataModelWithAssociations>
    )

    suspend fun getAllBudgets(): List<BudgetDataModel>

    suspend fun getBudgetWithAssociations(budgetId: Int): BudgetDataModelWithAssociations?

    suspend fun getAllBudgetsWithAssociations(): List<BudgetDataModelWithAssociations>

}