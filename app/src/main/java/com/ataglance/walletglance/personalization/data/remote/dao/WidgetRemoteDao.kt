package com.ataglance.walletglance.personalization.data.remote.dao

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.remote.FirestoreAdapter
import com.ataglance.walletglance.personalization.data.remote.model.WidgetRemoteEntity

class WidgetRemoteDao(
    private val firestoreAdapter: FirestoreAdapter<WidgetRemoteEntity>
) {

    suspend fun upsertWidgets(widgets: List<WidgetRemoteEntity>, userId: String) {
        firestoreAdapter.upsertEntities(entities = widgets, userId = userId)
    }

    suspend fun synchroniseWidgets(
        widgetsToSync: EntitiesToSync<WidgetRemoteEntity>,
        userId: String
    ) {
        firestoreAdapter.synchroniseEntities(
            toDelete = widgetsToSync.toDelete, toUpsert = widgetsToSync.toUpsert, userId = userId
        )
    }

    suspend fun getWidgetsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<WidgetRemoteEntity> {
        return firestoreAdapter
            .getEntitiesAfterTimestamp(timestamp = timestamp, userId = userId)
            .let { entities ->
                EntitiesToSync.fromEntities(entities = entities, deletedPredicate = { it.deleted })
            }
    }

}