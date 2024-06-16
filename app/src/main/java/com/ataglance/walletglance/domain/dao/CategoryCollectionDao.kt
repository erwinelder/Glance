package com.ataglance.walletglance.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ataglance.walletglance.domain.entities.CategoryCollection

@Dao
interface CategoryCollectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceCollections(collectionList: List<CategoryCollection>)

    @Query("DELETE FROM CategoryCollection WHERE id IN (:idList)")
    suspend fun deleteCollectionsByIds(idList: List<Int>)

    @Query("DELETE FROM CategoryCollection")
    suspend fun deleteAllCollections()

    @Query("SELECT * FROM CategoryCollection")
    suspend fun getAllCollections(): List<CategoryCollection>

}