package com.ataglance.walletglance.domain.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ataglance.walletglance.domain.entities.CategoryCollection

@Dao
interface CategoryCollectionDao {

    @Upsert
    suspend fun upsertCollections(collectionList: List<CategoryCollection>)

    @Query("DELETE FROM CategoryCollection WHERE id IN (:idList)")
    suspend fun deleteCollectionsByIds(idList: List<Int>)

    @Query("DELETE FROM CategoryCollection")
    suspend fun deleteAllCollections()

    @Query("SELECT * FROM CategoryCollection")
    suspend fun getAllCollections(): List<CategoryCollection>

}