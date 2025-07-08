package com.ataglance.walletglance.categoryCollection.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociationEntity
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryCollectionLocalDao {

    @Upsert
    suspend fun upsertCollections(collections: List<CategoryCollectionEntity>)

    @Delete
    suspend fun deleteCollections(collections: List<CategoryCollectionEntity>)

    @Query("DELETE FROM category_collection")
    suspend fun deleteAllCategoryCollections()

    @Query("SELECT * FROM category_collection WHERE timestamp > :timestamp")
    suspend fun getCollectionsAfterTimestamp(timestamp: Long): List<CategoryCollectionEntity>

    @Query("SELECT * FROM category_collection")
    fun getAllCollectionsAsFlow(): Flow<List<CategoryCollectionEntity>>

    @Query("SELECT * FROM category_collection")
    suspend fun getAllCollections(): List<CategoryCollectionEntity>


    @Upsert
    suspend fun upsertCollectionCategoryAssociations(
        associations: List<CategoryCollectionCategoryAssociationEntity>
    )

    @Delete
    suspend fun deleteCollectionCategoryAssociations(
        associations: List<CategoryCollectionCategoryAssociationEntity>
    )

    @Query("SELECT * FROM category_collection_category_association WHERE collectionId IN (:collectionIds)")
    suspend fun getCollectionCategoryAssociations(
        collectionIds: List<Int>
    ): List<CategoryCollectionCategoryAssociationEntity>

    @Query("SELECT * FROM category_collection_category_association")
    fun getAllCollectionCategoryAssociationsAsFlow(): Flow<List<CategoryCollectionCategoryAssociationEntity>>

    @Query("SELECT * FROM category_collection_category_association")
    suspend fun getAllCollectionCategoryAssociations(): List<CategoryCollectionCategoryAssociationEntity>


    @Transaction
    suspend fun upsertCollectionsAndAssociations(
        collections: List<CategoryCollectionEntity>,
        associations: List<CategoryCollectionCategoryAssociationEntity>
    ) {
        upsertCollections(collections = collections)
        upsertCollectionCategoryAssociations(associations = associations)
    }

    @Transaction
    suspend fun deleteAndUpsertCollectionsAndAssociations(
        collectionsToDelete: List<CategoryCollectionEntity>,
        collectionsToUpsert: List<CategoryCollectionEntity>,
        associationsToUpsert: List<CategoryCollectionCategoryAssociationEntity>,
        associationsToDelete: List<CategoryCollectionCategoryAssociationEntity>
    ) {
        deleteCollections(collections = collectionsToDelete)
        upsertCollections(collections = collectionsToUpsert)
        upsertCollectionCategoryAssociations(associations = associationsToUpsert)
        deleteCollectionCategoryAssociations(associations = associationsToDelete)
    }

}