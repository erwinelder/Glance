package com.ataglance.walletglance.budget.data.remote.dao

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.remote.FirestoreAdapter
import com.ataglance.walletglance.budget.data.remote.model.BudgetOnWidgetRemoteEntity

class BudgetOnWidgetRemoteDao(
    private val firestoreAdapter: FirestoreAdapter<BudgetOnWidgetRemoteEntity>
) {

    suspend fun upsertBudgetsOnWidgets(budgets: List<BudgetOnWidgetRemoteEntity>, userId: String) {
        firestoreAdapter.upsertEntities(entities = budgets, userId = userId)
    }

    suspend fun synchroniseBudgetsOnWidget(
        budgetsToSync: EntitiesToSync<BudgetOnWidgetRemoteEntity>,
        userId: String
    ) {
        firestoreAdapter.synchroniseEntities(
            toDelete = budgetsToSync.toDelete, toUpsert = budgetsToSync.toUpsert, userId = userId
        )
    }

    suspend fun getBudgetsOnWidgetAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<BudgetOnWidgetRemoteEntity> {
        return firestoreAdapter
            .getEntitiesAfterTimestamp(timestamp = timestamp, userId = userId)
            .let { entities ->
                EntitiesToSync.fromEntities(entities = entities, deletedPredicate = { it.deleted })
            }
    }

}