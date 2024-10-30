package com.ataglance.walletglance.core.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.ataglance.walletglance.core.utils.getNowDateTimeLong
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
        val timestamp = getNowDateTimeLong()

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
            timestamp = timestamp,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
        associationRemoteSource?.deleteAndUpsertEntities(
            entitiesToDelete = associationsToDelete,
            entitiesToUpsert = associationsToUpsert,
            timestamp = timestamp,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
    }

    suspend fun getEntitiesAndAssociations(
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    ): Pair<List<E>, List<A>> {
        return selectEntities() to selectAssociations()
    }

    private suspend fun selectEntities(): List<E> {
        val localTimestamp = entityLocalSource.getLastModifiedTime()
        val remoteTimestamp = entityRemoteSource?.getLastModifiedTime() ?: localTimestamp

        if (needToSyncData(entityLocalSource, entityRemoteSource)) {
            syncData(
                localSource = entityLocalSource,
                remoteDataSource = entityRemoteSource,
                localTimestamp = localTimestamp,
                remoteTimestamp = remoteTimestamp
            )
        }

        return entityLocalSource.getAllEntities().first()
    }

    private suspend fun selectAssociations(): List<A> {
        val localTimestamp = associationLocalSource.getLastModifiedTime()
        val remoteTimestamp = associationRemoteSource?.getLastModifiedTime() ?: localTimestamp

        if (needToSyncData(associationLocalSource, associationRemoteSource)) {
            syncData(
                localSource = associationLocalSource,
                remoteDataSource = associationRemoteSource,
                localTimestamp = localTimestamp,
                remoteTimestamp = remoteTimestamp
            )
        }

        return associationLocalSource.getAllEntities().first()
    }

    private suspend fun <T> needToSyncData(
        localSource: BaseLocalDataSource<T>,
        remoteDataSource: BaseRemoteDataSource<T>?
    ): Boolean {
        val localTimestamp = localSource.getLastModifiedTime()
        val remoteTimestamp = remoteDataSource?.getLastModifiedTime() ?: return false
        return remoteTimestamp > localTimestamp
    }

    private suspend fun <T> syncData(
        localSource: BaseLocalDataSource<T>,
        remoteDataSource: BaseRemoteDataSource<T>?,
        localTimestamp: Long,
        remoteTimestamp: Long
    ) {
        remoteDataSource?.getEntitiesAfterTimestamp(localTimestamp)?.first()?.let { dataToSync ->
            localSource.deleteAndUpsertEntities(
                entitiesToDeleteAndUpsert = dataToSync, timestamp = remoteTimestamp
            )
        }
    }

}