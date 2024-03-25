package com.ataglance.walletglance.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceCategories(categoryList: List<Category>)

    @Query("DELETE FROM Category WHERE id IN (:idList)")
    suspend fun deleteCategoriesByIds(idList: List<Int>)

    @Query("DELETE FROM Category")
    suspend fun deleteAllCategories()

    @Transaction
    suspend fun deleteAndUpsertCategories(
        idListToDelete: List<Int>,
        categoryListToUpsert: List<Category>
    ) {
        deleteCategoriesByIds(idListToDelete)
        insertOrReplaceCategories(categoryListToUpsert)
    }

    @Query("SELECT * FROM Category")
    fun getAllCategories(): Flow<List<Category>>

}