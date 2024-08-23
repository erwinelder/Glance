package com.ataglance.walletglance.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.data.local.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Upsert
    suspend fun upsertCategories(categoryList: List<CategoryEntity>)

    @Query("DELETE FROM Category WHERE id IN (:idList)")
    suspend fun deleteCategoriesByIds(idList: List<Int>)

    @Query("DELETE FROM Category")
    suspend fun deleteAllCategories()

    @Transaction
    suspend fun deleteAndUpsertCategories(
        idListToDelete: List<Int>,
        categoryListToUpsert: List<CategoryEntity>
    ) {
        deleteCategoriesByIds(idListToDelete)
        upsertCategories(categoryListToUpsert)
    }

    @Query("SELECT * FROM Category")
    fun getAllCategories(): Flow<List<CategoryEntity>>

}