package com.ataglance.walletglance.core.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.core.data.local.source.BaseLocalDataSource
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import kotlinx.coroutines.flow.first

interface BaseEntityAndAssociationRepository<E, A> {

    val entityLocalSource: BaseLocalDataSource<E>
    val entityRemoteSource: BaseRemoteDataSource<E>?

    val associationLocalSource: BaseLocalDataSource<A>
    val associationRemoteSource: BaseRemoteDataSource<A>?

    @Transaction
    suspend fun deleteAndUpsertEntitiesAndDeleteAndUpsertAssociations(
        entitiesToDelete: List<E>,
        entitiesToUpsert: List<E>,
        associationsToDelete: List<A>,
        associationsToUpsert: List<A>,
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    ) {
        val timestamp = getCurrentTimestamp()

        entityLocalSource.deleteAndUpsertEntities(
            entitiesToDelete = entitiesToDelete,
            entitiesToUpsert = entitiesToUpsert,
            timestamp = timestamp
        )
        associationLocalSource.deleteAndUpsertEntities(
            entitiesToDelete = associationsToDelete,
            entitiesToUpsert = associationsToUpsert,
            timestamp = timestamp
        )

        entityRemoteSource?.deleteAndUpsertEntities(
            entitiesToDelete = entitiesToDelete,
            entitiesToUpsert = entitiesToUpsert,
            timestamp = timestamp
        )
        associationRemoteSource?.deleteAndUpsertEntities(
            entitiesToDelete = associationsToDelete,
            entitiesToUpsert = associationsToUpsert,
            timestamp = timestamp
        )
    }

    suspend fun getEntitiesAndAssociations(
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    ): Pair<List<E>, List<A>> {
        val entities = selectEntities()
        val associations = selectAssociations()
        return entities to associations
    }

    private suspend fun selectEntities(): List<E> {
        syncDataIfNeeded(entityLocalSource, entityRemoteSource)
        return entityLocalSource.getAllEntities().first()
    }

    private suspend fun selectAssociations(): List<A> {
        syncDataIfNeeded(associationLocalSource, associationRemoteSource)
        return associationLocalSource.getAllEntities().first()
    }

    private suspend fun <T> syncDataIfNeeded(
        localSource: BaseLocalDataSource<T>,
        remoteSource: BaseRemoteDataSource<T>?
    ) {
        val remoteTimestamp = remoteSource?.getLastModifiedTime()
        if (remoteSource != null && remoteTimestamp == null) {
            val localTimestamp = localSource.getLastModifiedTime() ?: return

            val entities = localSource.getAllEntities().first()
            remoteSource.upsertEntities(entityList = entities, timestamp = localTimestamp)
            return
        }
        remoteTimestamp ?: return

        val localTimestamp = localSource.getLastModifiedTime() ?: 0L

        if (localTimestamp >= remoteTimestamp) return

        remoteSource.getEntitiesAfterTimestamp(timestamp = localTimestamp).first()
            .takeIf { it.isNotEmpty() }
            ?.let { dataToSync ->
                localSource.deleteAndUpsertEntities(
                    entitiesToDeleteAndUpsert = dataToSync,
                    timestamp = remoteTimestamp
                )
            }
    }

}