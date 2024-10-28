package com.ataglance.walletglance.core.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.remote.BaseRemoteDataSource
import com.ataglance.walletglance.core.utils.getNowDateTimeLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach

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

    fun getEntitiesAndAssociations(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ): Flow<Pair<List<E>, List<A>>> = flow {
        try {

            val entitiesLocalTimestamp = entityLocalSource.getLastModifiedTime()
            val entitiesRemoteTimestamp = entityRemoteSource?.getLastModifiedTime()
                ?: entitiesLocalTimestamp
            val associationsLocalTimestamp = associationLocalSource.getLastModifiedTime()
            val associationsRemoteTimestamp = associationRemoteSource?.getLastModifiedTime()
                ?: associationsLocalTimestamp

            val entitiesFlow = selectEntitiesFlow(
                localTimestamp = entitiesLocalTimestamp,
                remoteTimestamp = entitiesRemoteTimestamp
            )
            val associationsFlow = selectAssociationsFlow(
                localTimestamp = associationsLocalTimestamp,
                remoteTimestamp = associationsRemoteTimestamp
            )

            emitAll(combine(entitiesFlow, associationsFlow) { entities, associations ->
                Pair(entities, associations)
            })

        } catch (e: Exception) {
            onFailureListener(e)
        }
    }.flowOn(Dispatchers.IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun selectEntitiesFlow(
        localTimestamp: Long, remoteTimestamp: Long
    ): Flow<List<E>> {
        val entityRemoteDataSource = entityRemoteSource

        return if (entityRemoteDataSource != null && remoteTimestamp > localTimestamp) {
            entityRemoteDataSource.getEntitiesAfterTimestamp(localTimestamp)
                .onEach { entitiesToDeleteAndUpsert ->
                    entityLocalSource.deleteAndUpsertEntities(
                        entitiesToDeleteAndUpsert = entitiesToDeleteAndUpsert,
                        timestamp = remoteTimestamp
                    )
                }
                .flatMapConcat { entityLocalSource.getAllEntities() }
        } else {
            entityLocalSource.getAllEntities()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun selectAssociationsFlow(
        localTimestamp: Long, remoteTimestamp: Long
    ): Flow<List<A>> {
        val associationRemoteDataSource = associationRemoteSource

        return if (associationRemoteDataSource != null && remoteTimestamp > localTimestamp) {
            associationRemoteDataSource.getEntitiesAfterTimestamp(localTimestamp)
                .onEach { entitiesToDeleteAndUpsert ->
                    associationLocalSource.deleteAndUpsertEntities(
                        entitiesToDeleteAndUpsert = entitiesToDeleteAndUpsert,
                        timestamp = remoteTimestamp
                    )
                }
                .flatMapConcat { associationLocalSource.getAllEntities() }
        } else {
            associationLocalSource.getAllEntities()
        }
    }

}