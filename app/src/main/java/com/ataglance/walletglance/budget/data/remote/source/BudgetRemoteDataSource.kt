package com.ataglance.walletglance.budget.data.remote.source

import com.ataglance.walletglance.budget.data.remote.model.BudgetAccountRemoteAssociation
import com.ataglance.walletglance.budget.data.remote.model.BudgetRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSync

interface BudgetRemoteDataSource {

    suspend fun getBudgetUpdateTime(userId: String): Long?

    suspend fun saveBudgetUpdateTime(timestamp: Long, userId: String)

    suspend fun getBudgetsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<BudgetRemoteEntity>


    suspend fun getBudgetAccountAssociationUpdateTime(userId: String): Long?

    suspend fun saveBudgetAccountAssociationUpdateTime(timestamp: Long, userId: String)

    suspend fun getBudgetAccountAssociationsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<BudgetAccountRemoteAssociation>


    suspend fun upsertBudgetsAndAssociations(
        budgets: List<BudgetRemoteEntity>,
        associations: List<BudgetAccountRemoteAssociation>,
        timestamp: Long,
        userId: String
    )

    suspend fun synchroniseBudgetsAndAssociations(
        budgetsToSync: EntitiesToSync<BudgetRemoteEntity>,
        associationsToSync: EntitiesToSync<BudgetAccountRemoteAssociation>,
        timestamp: Long,
        userId: String
    )

}