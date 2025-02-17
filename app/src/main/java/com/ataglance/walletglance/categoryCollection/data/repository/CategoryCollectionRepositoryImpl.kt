package com.ataglance.walletglance.categoryCollection.data.repository

import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import com.ataglance.walletglance.categoryCollection.data.local.source.CategoryCollectionLocalDataSource
import com.ataglance.walletglance.categoryCollection.data.mapper.toLocalAssociation
import com.ataglance.walletglance.categoryCollection.data.mapper.toLocalEntity
import com.ataglance.walletglance.categoryCollection.data.mapper.toRemoteAssociation
import com.ataglance.walletglance.categoryCollection.data.mapper.toRemoteEntity
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionCategoryRemoteAssociation
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionRemoteEntity
import com.ataglance.walletglance.categoryCollection.data.remote.source.CategoryCollectionRemoteDataSource
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.utils.synchroniseData
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CategoryCollectionRepositoryImpl(
    private val localSource: CategoryCollectionLocalDataSource,
    private val remoteSource: CategoryCollectionRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : CategoryCollectionRepository {

    private suspend fun synchroniseCollections() {
        val userId = syncHelper.getUserIdForSync(
            TableName.CategoryCollection,
            TableName.CategoryCollectionCategoryAssociation
        ) ?: return

        synchroniseData(
            localUpdateTimeGetter = localSource::getCategoryCollectionUpdateTime,
            remoteUpdateTimeGetter = { remoteSource.getCategoryCollectionUpdateTime(userId = userId) },
            remoteDataGetter = { timestamp ->
                remoteSource.getCategoryCollectionsAfterTimestamp(timestamp = timestamp, userId = userId)
            },
            remoteDataToLocalDataMapper = CategoryCollectionRemoteEntity::toLocalEntity,
            localSynchroniser = localSource::synchroniseCategoryCollections
        )
    }

    private suspend fun synchroniseCollectionCategoryAssociations() {
        val userId = syncHelper.getUserIdForSync(
            TableName.CategoryCollection,
            TableName.CategoryCollectionCategoryAssociation
        ) ?: return

        synchroniseData(
            localUpdateTimeGetter = localSource::getCollectionCategoryAssociationUpdateTime,
            remoteUpdateTimeGetter = {
                remoteSource.getCollectionCategoryAssociationUpdateTime(userId = userId)
            },
            remoteDataGetter = { timestamp ->
                remoteSource.getCollectionCategoryAssociationsAfterTimestamp(
                    timestamp = timestamp, userId = userId
                )
            },
            remoteDataToLocalDataMapper = CategoryCollectionCategoryRemoteAssociation::toLocalAssociation,
            localSynchroniser = localSource::synchroniseCollectionCategoryAssociations
        )
    }


    override suspend fun deleteAndUpsertCollectionsAndAssociations(
        collectionsToDelete: List<CategoryCollectionEntity>,
        collectionsToUpsert: List<CategoryCollectionEntity>,
        associationsToDelete: List<CategoryCollectionCategoryAssociation>,
        associationsToUpsert: List<CategoryCollectionCategoryAssociation>
    ) {
        val timestamp = getCurrentTimestamp()
        val collectionsToSync = EntitiesToSync(
            toDelete = collectionsToDelete, toUpsert = collectionsToUpsert
        )
        val associationsToSync = EntitiesToSync(
            toDelete = associationsToDelete, toUpsert = associationsToUpsert
        )

        localSource.synchroniseCollectionsAndAssociations(
            collectionsToSync = collectionsToSync,
            associationsToSync = associationsToSync,
            timestamp = timestamp
        )
        syncHelper.tryToSyncToRemote(
            TableName.CategoryCollection,
            TableName.CategoryCollectionCategoryAssociation
        ) { userId ->
            remoteSource.synchroniseCollectionsAndAssociations(
                collectionsToSync = collectionsToSync.map { deleted ->
                    toRemoteEntity(updateTime = timestamp, deleted = deleted)
                },
                associationsToSync = associationsToSync.map { deleted ->
                    toRemoteAssociation(updateTime = timestamp, deleted = deleted)
                },
                timestamp = timestamp,
                userId = userId
            )
        }
    }

    override suspend fun deleteAllCategoryCollectionsLocally() {
        val timestamp = getCurrentTimestamp()
        localSource.deleteAllCategoryCollections(timestamp = timestamp)
    }

    override fun getAllCollectionsAndAssociationsFlow(
    ): Flow<Pair<List<CategoryCollectionEntity>, List<CategoryCollectionCategoryAssociation>>> = flow {
        coroutineScope {
            launch {
                synchroniseCollections()
                synchroniseCollectionCategoryAssociations()
            }
            combine(
                localSource.getAllCategoryCollectionsFlow(),
                localSource.getAllCollectionCategoryAssociationsFlow()
            ) { collections, associations ->
                collections to associations
            }.collect(::emit)
        }
    }

    override suspend fun getAllCollectionsAndAssociations(
    ): Pair<List<CategoryCollectionEntity>, List<CategoryCollectionCategoryAssociation>> {
        synchroniseCollections()
        synchroniseCollectionCategoryAssociations()

        val collections = localSource.getAllCategoryCollections()
        val associations = localSource.getAllCollectionCategoryAssociations()

        return collections to associations
    }

}