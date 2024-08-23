package com.ataglance.walletglance.categoryCollection.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity

@Dao
interface CategoryCollectionDao {

    @Upsert
    suspend fun upsertCollections(collectionList: List<CategoryCollectionEntity>)

    @Query("DELETE FROM CategoryCollection WHERE id IN (:idList)")
    suspend fun deleteCollectionsByIds(idList: List<Int>)

    @Query("DELETE FROM CategoryCollection")
    suspend fun deleteAllCollections()

    @Query("SELECT * FROM CategoryCollection")
    suspend fun getAllCollections(): List<CategoryCollectionEntity>

}