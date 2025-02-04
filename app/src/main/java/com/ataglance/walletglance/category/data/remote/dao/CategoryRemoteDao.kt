package com.ataglance.walletglance.category.data.remote.dao

import com.ataglance.walletglance.category.data.remote.model.CategoryRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.remote.FirestoreAdapter

class CategoryRemoteDao(
    private val firestoreAdapter: FirestoreAdapter<CategoryRemoteEntity>
) {

    suspend fun upsertCategories(categories: List<CategoryRemoteEntity>, userId: String) {
        firestoreAdapter.upsertEntities(entities = categories, userId = userId)
    }

    suspend fun synchroniseCategories(
        categoriesToSync: EntitiesToSync<CategoryRemoteEntity>,
        userId: String
    ) {
        firestoreAdapter.synchroniseEntities(
            toDelete = categoriesToSync.toDelete,
            toUpsert = categoriesToSync.toUpsert,
            userId = userId
        )
    }

    suspend fun getCategoriesAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<CategoryRemoteEntity> {
        return firestoreAdapter
            .getEntitiesAfterTimestamp(timestamp = timestamp, userId = userId)
            .let { entities ->
                EntitiesToSync.fromEntities(entities = entities, deletedPredicate = { it.deleted })
            }
    }

}