package com.ataglance.walletglance.categoryCollection.data.local.source

import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntityWithAssociations
import kotlinx.coroutines.flow.Flow

interface CategoryCollectionLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun deleteUpdateTime()

    suspend fun deleteAllCategoryCollections()

    suspend fun upsertCollectionsWithAssociations(
        collectionsWithAssociations: List<CategoryCollectionEntityWithAssociations>,
        timestamp: Long
    )

    suspend fun deleteCollectionsWithAssociations(
        collectionsWithAssociations: List<CategoryCollectionEntityWithAssociations>
    )

    suspend fun deleteAndUpsertCollectionsWithAssociations(
        toDelete: List<CategoryCollectionEntityWithAssociations>,
        toUpsert: List<CategoryCollectionEntityWithAssociations>,
        timestamp: Long
    )

    suspend fun getAllCollections(): List<CategoryCollectionEntity>

    suspend fun getCollectionsWithAssociationsAfterTimestamp(
        timestamp: Long
    ): List<CategoryCollectionEntityWithAssociations>

    fun getAllCollectionsWithAssociationsAsFlow(
    ): Flow<List<CategoryCollectionEntityWithAssociations>>

    suspend fun getAllCollectionsWithAssociations(): List<CategoryCollectionEntityWithAssociations>

}