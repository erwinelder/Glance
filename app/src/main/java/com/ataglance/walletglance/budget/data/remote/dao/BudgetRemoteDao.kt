package com.ataglance.walletglance.budget.data.remote.dao

import com.ataglance.walletglance.budget.data.remote.model.BudgetAccountRemoteAssociation
import com.ataglance.walletglance.budget.data.remote.model.BudgetRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.remote.FirestoreAdapter

class BudgetRemoteDao(
    private val budgetFirestoreAdapter: FirestoreAdapter<BudgetRemoteEntity>,
    private val associationFirestoreAdapter: FirestoreAdapter<BudgetAccountRemoteAssociation>
) {

    suspend fun getBudgetsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<BudgetRemoteEntity> {
        return budgetFirestoreAdapter
            .getEntitiesAfterTimestamp(timestamp = timestamp, userId = userId)
            .let { entities ->
                EntitiesToSync.fromEntities(entities = entities, deletedPredicate = { it.deleted })
            }
    }

    suspend fun getBudgetAccountAssociationsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<BudgetAccountRemoteAssociation> {
        return associationFirestoreAdapter
            .getEntitiesAfterTimestamp(timestamp = timestamp, userId = userId)
            .let { entities ->
                EntitiesToSync.fromEntities(entities = entities, deletedPredicate = { it.deleted })
            }
    }


    suspend fun synchroniseBudgetsAndAssociations(
        budgetsToSync: EntitiesToSync<BudgetRemoteEntity>,
        associationsToSync: EntitiesToSync<BudgetAccountRemoteAssociation>,
        userId: String
    ) {
        budgetFirestoreAdapter.synchroniseEntities(
            toDelete = budgetsToSync.toDelete,
            toUpsert = budgetsToSync.toUpsert,
            userId = userId
        )
        associationFirestoreAdapter.synchroniseEntities(
            toDelete = associationsToSync.toDelete,
            toUpsert = associationsToSync.toUpsert,
            userId = userId
        )
    }

}