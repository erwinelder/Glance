package com.ataglance.walletglance.categoryCollection.data.remote.dao

import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionCategoryRemoteAssociation
import com.ataglance.walletglance.categoryCollection.data.remote.model.CategoryCollectionRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.remote.FirestoreAdapter

class CategoryCollectionRemoteDao(
    private val categoryCollectionFirestoreAdapter: FirestoreAdapter<CategoryCollectionRemoteEntity>,
    private val associationFirestoreAdapter: FirestoreAdapter<CategoryCollectionCategoryRemoteAssociation>
) {

    suspend fun getCollectionsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<CategoryCollectionRemoteEntity> {
        return categoryCollectionFirestoreAdapter
            .getEntitiesAfterTimestamp(timestamp = timestamp, userId = userId)
            .let { entities ->
                EntitiesToSync.fromEntities(entities = entities, deletedPredicate = { it.deleted })
            }
    }

    suspend fun getCollectionCategoryAssociationsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<CategoryCollectionCategoryRemoteAssociation> {
        return associationFirestoreAdapter
            .getEntitiesAfterTimestamp(timestamp = timestamp, userId = userId)
            .let { entities ->
                EntitiesToSync.fromEntities(entities = entities, deletedPredicate = { it.deleted })
            }
    }


    suspend fun synchroniseCollectionsAndAssociations(
        collectionsToSync: EntitiesToSync<CategoryCollectionRemoteEntity>,
        associationsToSync: EntitiesToSync<CategoryCollectionCategoryRemoteAssociation>,
        userId: String
    ) {
        categoryCollectionFirestoreAdapter.synchroniseEntities(
            toDelete = collectionsToSync.toDelete,
            toUpsert = collectionsToSync.toUpsert,
            userId = userId
        )
        associationFirestoreAdapter.synchroniseEntities(
            toDelete = associationsToSync.toDelete,
            toUpsert = associationsToSync.toUpsert,
            userId = userId
        )
    }

}