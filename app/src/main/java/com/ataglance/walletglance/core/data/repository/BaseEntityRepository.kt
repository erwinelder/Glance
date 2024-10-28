package com.ataglance.walletglance.core.data.repository

import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.ataglance.walletglance.core.utils.getNowDateTimeLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    suspend fun deleteAndUpsertCategories(
        listToDelete: List<T>,
        listToUpsert: List<T>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val timestamp = getNowDateTimeLong()

        localSource.deleteAndUpsertEntities(
            entitiesToDelete = listToDelete,
            entitiesToUpsert = listToUpsert,
            timestamp = timestamp
        )
        remoteSource?.deleteAndUpsertEntities(
            entitiesToDelete = listToDelete,
            entitiesToUpsert = listToUpsert,
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
    ): Flow<List<T>> = flow {
        try {
            val remoteDataSource = remoteSource

            val localTimestamp = localSource.getLastModifiedTime()
            val remoteTimestamp = remoteDataSource?.getLastModifiedTime() ?: localTimestamp

            if (remoteDataSource != null && remoteTimestamp > localTimestamp) {
                remoteDataSource.getEntitiesAfterTimestamp(localTimestamp)
                    .collect { entitiesToDeleteAndUpsert ->

                        localSource.deleteAndUpsertEntities(
                            entitiesToDeleteAndUpsert = entitiesToDeleteAndUpsert,
                            timestamp = remoteTimestamp
                        )
                        localSource.getAllEntities().collect { localList ->
                            emit(localList)
                            onSuccessListener()
                        }

                    }
            } else {
                localSource.getAllEntities().collect { localList ->
                    emit(localList)
                    onSuccessListener()
                }
            }

        } catch (e: Exception) {
            onFailureListener(e)
        }
    }.flowOn(Dispatchers.IO)

}