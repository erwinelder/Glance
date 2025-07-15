package com.ataglance.walletglance.categoryCollection.data.repository

import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntityWithAssociations
import com.ataglance.walletglance.categoryCollection.data.local.source.CategoryCollectionLocalDataSource
import com.ataglance.walletglance.categoryCollection.data.mapper.toDataModel
import com.ataglance.walletglance.categoryCollection.data.mapper.toDataModelWithAssociations
import com.ataglance.walletglance.categoryCollection.data.mapper.toDtoWithAssociations
import com.ataglance.walletglance.categoryCollection.data.mapper.toEntityWithAssociations
import com.ataglance.walletglance.categoryCollection.data.mapper.withAssociations
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionDataModel
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionDataModelWithAssociations
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionDtoWithAssociations
import com.ataglance.walletglance.categoryCollection.data.remote.source.CategoryCollectionRemoteDataSource
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class CategoryCollectionRepositoryImpl(
    private val localSource: CategoryCollectionLocalDataSource,
    private val remoteSource: CategoryCollectionRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : CategoryCollectionRepository {

    private suspend fun synchronizeCollections() {
        syncHelper.synchronizeData(
            tableName = TableName.CategoryCollection,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localDataGetter = { timestamp ->
                localSource.getCollectionsWithAssociationsAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, userId ->
                remoteSource.getCollectionsWithAssociationsAfterTimestamp(
                    timestamp = timestamp, userId = userId
                )
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertCollectionsWithAssociations(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, userId ->
                remoteSource.synchronizeCollectionsWithAssociations(
                    collections = data, timestamp = timestamp, userId = userId
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = CategoryCollectionEntityWithAssociations::toDtoWithAssociations,
            queryDtoToEntityMapper = CategoryCollectionDtoWithAssociations::toEntityWithAssociations,
        )
    }

    override suspend fun deleteAllCollectionsLocally() {
        localSource.deleteAllCategoryCollections()
    }

    override suspend fun deleteAndUpsertCollectionsWithAssociations(
        toDelete: List<CategoryCollectionDataModel>,
        toUpsert: List<CategoryCollectionDataModelWithAssociations>
    ) {
        syncHelper.deleteAndUpsertData(
            toDelete = toDelete.map { it.withAssociations() },
            toUpsert = toUpsert,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localSoftCommand = { entities, timestamp ->
                localSource.upsertCollectionsWithAssociations(
                    collectionsWithAssociations = entities, timestamp = timestamp
                )
                entities
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndUpsertCollectionsWithAssociations(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities ->
                localSource.deleteCollectionsWithAssociations(collectionsWithAssociations = entities)
            },
            remoteSoftCommand = { dtos, timestamp, userId ->
                remoteSource.synchronizeCollectionsWithAssociations(
                    collections = dtos, timestamp = timestamp, userId = userId
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getCollectionsWithAssociationsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, userId, localTimestamp ->
                remoteSource.synchronizeCollectionsWithAssociationsAndGetAfterTimestamp(
                    collections = dtos,
                    timestamp = timestamp,
                    userId = userId,
                    localTimestamp = localTimestamp
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = CategoryCollectionDataModelWithAssociations::toEntityWithAssociations,
            dataModelToCommandDtoMapper = CategoryCollectionDataModelWithAssociations::toDtoWithAssociations,
            entityToCommandDtoMapper = CategoryCollectionEntityWithAssociations::toDtoWithAssociations,
            queryDtoToEntityMapper = CategoryCollectionDtoWithAssociations::toEntityWithAssociations
        )
    }

    override suspend fun getAllCollections(): List<CategoryCollectionDataModel> {
        synchronizeCollections()
        return localSource.getAllCollections().map { it.toDataModel() }
    }

    override fun getAllCollectionsWithAssociationsAsFlow(
    ): Flow<List<CategoryCollectionDataModelWithAssociations>> {
        return localSource.getAllCollectionsWithAssociationsAsFlow()
            .onStart { synchronizeCollections() }
            .map { collectionsWithAssociations ->
                collectionsWithAssociations.map { it.toDataModelWithAssociations() }
            }
    }

    override suspend fun getAllCollectionsWithAssociations(
    ): List<CategoryCollectionDataModelWithAssociations> {
        synchronizeCollections()
        return localSource.getAllCollectionsWithAssociations().map {
            it.toDataModelWithAssociations()
        }
    }

}