package com.ataglance.walletglance.budget.data.local.source

import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.data.local.model.BudgetEntityWithAssociations

interface BudgetLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun upsertBudgetsWithAssociations(
        budgetsWithAssociations: List<BudgetEntityWithAssociations>,
        timestamp: Long
    )

    suspend fun deleteBudgetsWithAssociations(
        budgetsWithAssociations: List<BudgetEntityWithAssociations>
    )

    suspend fun deleteAndUpsertBudgetsWithAssociations(
        toDelete: List<BudgetEntityWithAssociations>,
        toUpsert: List<BudgetEntityWithAssociations>,
        timestamp: Long
    )

    suspend fun getAllBudgets(): List<BudgetEntity>

    suspend fun getBudgetsWithAssociationsAfterTimestamp(
        timestamp: Long
    ): List<BudgetEntityWithAssociations>

    suspend fun getBudgetWithAssociations(budgetId: Int): BudgetEntityWithAssociations?

    suspend fun getAllBudgetsWithAssociations(): List<BudgetEntityWithAssociations>

}