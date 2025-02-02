package com.ataglance.walletglance.core.data.repository

import com.ataglance.walletglance.core.data.local.source.BaseLocalDataSource
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface BaseEntityRepository<T> {

    val localSource: BaseLocalDataSource<T>
    val remoteSource: BaseRemoteDataSource<T>?

    suspend fun upsertEntities(entityList: List<T>) {
        val timestamp = getCurrentTimestamp()
        localSource.upsertEntities(entityList = entityList, timestamp = timestamp)
        remoteSource?.upsertEntities(entityList = entityList, timestamp = timestamp)
    }

    suspend fun deleteAndUpsertEntities(
        toDelete: List<T>,
        toUpsert: List<T>
    ) {
        val timestamp = getCurrentTimestamp()

        localSource.deleteAndUpsertEntities(
            entitiesToDelete = toDelete,
            entitiesToUpsert = toUpsert,
            timestamp = timestamp
        )
        remoteSource?.deleteAndUpsertEntities(
            entitiesToDelete = toDelete,
            entitiesToUpsert = toUpsert,
            timestamp = timestamp
        )
    }

    suspend fun deleteAllEntities()

    suspend fun deleteAllEntitiesLocally()

    fun getAllEntities(): Flow<List<T>> = syncDataAndGetFlowWrapper {
        localSource.getAllEntities()
    }

    private suspend fun syncDataIfNeeded() {
        val remoteTimestamp = remoteSource?.getLastModifiedTime()
        if (remoteSource != null && remoteTimestamp == null) {
            val localTimestamp = localSource.getLastModifiedTime() ?: return

            val entities = localSource.getAllEntities().first()
            remoteSource?.upsertEntities(entityList = entities, timestamp = localTimestamp)
            return
        }
        remoteTimestamp ?: return

        val localTimestamp = localSource.getLastModifiedTime() ?: 0L

        if (localTimestamp >= remoteTimestamp) return

        remoteSource?.getEntitiesAfterTimestamp(timestamp = localTimestamp)?.collect { dataToSync ->
            localSource.deleteAndUpsertEntities(
                entitiesToDeleteAndUpsert = dataToSync,
                timestamp = remoteTimestamp
            )
        }
    }

    fun <F> syncDataAndGetFlowWrapper(flowSource: () -> Flow<F>): Flow<F> = flow {
        syncDataIfNeeded()
        emitAll(flowSource())
    }.flowOn(Dispatchers.IO)

    fun <F> syncAndExecute(onExecute: suspend FlowCollector<F>.() -> Unit): Flow<F> = flow {
        syncDataIfNeeded()
        onExecute()
    }.flowOn(Dispatchers.IO)

}