package com.ataglance.walletglance.category.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryLocalDao {

    @Upsert
    suspend fun upsertCategories(categories: List<CategoryEntity>)

    @Delete
    suspend fun deleteCategories(categories: List<CategoryEntity>)

    @Transaction
    suspend fun deleteAndUpsertCategories(
        toDelete: List<CategoryEntity>,
        toUpsert: List<CategoryEntity>
    ) {
        deleteCategories(toDelete)
        upsertCategories(toUpsert)
    }

    @Query("DELETE FROM Category")
    suspend fun deleteAllCategories()

    @Query("SELECT * FROM Category")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM Category WHERE type = :type")
    suspend fun getCategoriesByType(type: Char): List<CategoryEntity>

}