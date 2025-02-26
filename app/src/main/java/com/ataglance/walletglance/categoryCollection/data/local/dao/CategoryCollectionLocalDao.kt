package com.ataglance.walletglance.categoryCollection.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryCollectionLocalDao {

    @Upsert
    suspend fun upsertCollections(collections: List<CategoryCollectionEntity>)

    @Delete
    suspend fun deleteCollections(collections: List<CategoryCollectionEntity>)

    @Query("DELETE FROM CategoryCollection")
    suspend fun deleteAllCategoryCollections()

    @Transaction
    suspend fun deleteAndUpsertCollections(
        toDelete: List<CategoryCollectionEntity>,
        toUpsert: List<CategoryCollectionEntity>
    ) {
        deleteCollections(toDelete)
        upsertCollections(toUpsert)
    }

    @Query("SELECT * FROM CategoryCollection")
    fun getAllCollectionsFlow(): Flow<List<CategoryCollectionEntity>>

    @Query("SELECT * FROM CategoryCollection")
    suspend fun getAllCollections(): List<CategoryCollectionEntity>


    @Upsert
    suspend fun upsertCollectionCategoryAssociations(
        associations: List<CategoryCollectionCategoryAssociation>
    )

    @Delete
    suspend fun deleteCollectionCategoryAssociations(
        associations: List<CategoryCollectionCategoryAssociation>
    )

    @Transaction
    suspend fun deleteAndUpsertCollectionCategoryAssociations(
        toDelete: List<CategoryCollectionCategoryAssociation>,
        toUpsert: List<CategoryCollectionCategoryAssociation>
    ) {
        deleteCollectionCategoryAssociations(toDelete)
        upsertCollectionCategoryAssociations(toUpsert)
    }

    @Query("SELECT * FROM CategoryCollectionCategoryAssociation")
    fun getAllCollectionCategoryAssociationsFlow(): Flow<List<CategoryCollectionCategoryAssociation>>

    @Query("SELECT * FROM CategoryCollectionCategoryAssociation")
    suspend fun getAllCollectionCategoryAssociations(): List<CategoryCollectionCategoryAssociation>


    @Transaction
    suspend fun deleteAndUpsertCollectionsAndAssociations(
        collectionsToDelete: List<CategoryCollectionEntity>,
        collectionsToUpsert: List<CategoryCollectionEntity>,
        associationsToDelete: List<CategoryCollectionCategoryAssociation>,
        associationsToUpsert: List<CategoryCollectionCategoryAssociation>
    ) {
        deleteCollections(collectionsToDelete)
        upsertCollections(collectionsToUpsert)
        deleteCollectionCategoryAssociations(associationsToDelete)
        upsertCollectionCategoryAssociations(associationsToUpsert)
    }

}