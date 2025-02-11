package com.ataglance.walletglance.navigation.data.remote.dao

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.remote.FirestoreAdapter
import com.ataglance.walletglance.navigation.data.remote.model.NavigationButtonRemoteEntity

class NavigationButtonRemoteDao(
    private val firestoreAdapter: FirestoreAdapter<NavigationButtonRemoteEntity>
) {

    suspend fun upsertNavigationButtons(
        navigationButtons: List<NavigationButtonRemoteEntity>,
        userId: String
    ) {
        firestoreAdapter.upsertEntities(entities = navigationButtons, userId = userId)
    }

    suspend fun getNavigationButtonsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<NavigationButtonRemoteEntity> {
        return firestoreAdapter
            .getEntitiesAfterTimestamp(timestamp = timestamp, userId = userId)
            .let { entities ->
                EntitiesToSync.fromEntities(entities = entities, deletedPredicate = { it.deleted })
            }
    }

}