package com.ataglance.walletglance.budget.data.local.source

import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSync

interface BudgetLocalDataSource {

    suspend fun getBudgetUpdateTime(): Long?

    suspend fun saveBudgetUpdateTime(timestamp: Long)

    suspend fun synchroniseBudgets(budgetsToSync: EntitiesToSync<BudgetEntity>, timestamp: Long)

    suspend fun getBudget(id: Int): BudgetEntity?

    suspend fun getAllBudgets(): List<BudgetEntity>


    suspend fun getBudgetAccountAssociationUpdateTime(): Long?

    suspend fun saveBudgetAccountAssociationUpdateTime(timestamp: Long)

    suspend fun synchroniseBudgetAccountAssociations(
        associationsToSync: EntitiesToSync<BudgetAccountAssociation>,
        timestamp: Long
    )

    suspend fun getBudgetAccountAssociations(budgetId: Int): List<BudgetAccountAssociation>

    suspend fun getAllBudgetAccountAssociations(): List<BudgetAccountAssociation>


    suspend fun deleteBudgets(budgets: List<BudgetEntity>, timestamp: Long)

    suspend fun synchroniseBudgetsAndAssociations(
        budgetsToSync: EntitiesToSync<BudgetEntity>,
        associationsToSync: EntitiesToSync<BudgetAccountAssociation>,
        timestamp: Long
    )

}