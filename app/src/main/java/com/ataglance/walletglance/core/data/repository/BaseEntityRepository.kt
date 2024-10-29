package com.ataglance.walletglance.core.data.repository

import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.ataglance.walletglance.core.utils.getNowDateTimeLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface BaseEntityRepository<T> {

    val localSource: BaseLocalDataSource<T>
    val remoteSource: BaseRemoteDataSource<T>?

    suspend fun upsertEntities(
        entityList: List<T>,
        onSuccessListener: () -> Unit = {},
        onFailureListener: (Exception) -> Unit = {}
    ) {
        val timestamp = getNowDateTimeLong()
        localSource.upsertEntities(entityList = entityList, timestamp = timestamp)
        remoteSource?.upsertEntities(entityList = entityList, timestamp = timestamp)
    }

    suspend fun deleteAndUpsertEntities(
        toDelete: List<T>,
        toUpsert: List<T>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val timestamp = getNowDateTimeLong()

        localSource.deleteAndUpsertEntities(
            entitiesToDelete = toDelete,
            entitiesToUpsert = toUpsert,
            timestamp = timestamp
        )
        remoteSource?.deleteAndUpsertEntities(
            entitiesToDelete = toDelete,
            entitiesToUpsert = toUpsert,
            timestamp = timestamp,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
    }

    suspend fun deleteAllEntities(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    )

    fun getAllEntities(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ): Flow<List<T>> = syncDataAndGetFlowWrapper(
        flowSource = { localSource.getAllEntities() },
        onSuccessListener = onSuccessListener,
        onFailureListener = onFailureListener
    )

    suspend fun needToSyncData(): Boolean {
        val localTimestamp = localSource.getLastModifiedTime()
        val remoteTimestamp = remoteSource?.getLastModifiedTime() ?: return false

        return remoteTimestamp > localTimestamp
    }

    suspend fun syncDataFromRemoteSource() {
        val localTimestamp = localSource.getLastModifiedTime()
        val remoteTimestamp = remoteSource?.getLastModifiedTime() ?: return

        remoteSource?.getEntitiesAfterTimestamp(localTimestamp)?.collect { dataToSync ->

            localSource.deleteAndUpsertEntities(
                entitiesToDeleteAndUpsert = dataToSync,
                timestamp = remoteTimestamp
            )

        }
    }

    fun <F> syncDataAndGetFlowWrapper(
        flowSource: () -> Flow<F>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ): Flow<F> = flow {
        try {
            if (needToSyncData()) {
                syncDataFromRemoteSource()
            }
            emitAll(flowSource())
            onSuccessListener()
        } catch (e: Exception) {
            onFailureListener(e)
        }
    }.flowOn(Dispatchers.IO)

    fun <F> syncAndExecute(
        onExecute: suspend FlowCollector<F>.() -> Unit,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ): Flow<F> = flow {
        try {
            if (needToSyncData()) {
                syncDataFromRemoteSource()
            }
            onExecute()
            onSuccessListener()
        } catch (e: Exception) {
            onFailureListener(e)
        }
    }.flowOn(Dispatchers.IO)

}