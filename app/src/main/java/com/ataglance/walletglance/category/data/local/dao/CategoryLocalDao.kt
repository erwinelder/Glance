package com.ataglance.walletglance.category.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryLocalDao {

    @Insert
    suspend fun insertCategories(categories: List<CategoryEntity>): List<Long>

    @Upsert
    suspend fun upsertCategories(categories: List<CategoryEntity>)

    @Transaction
    suspend fun saveCategories(categories: List<CategoryEntity>): List<CategoryEntity> {
        val (toInsert, toUpsert) = categories.partition { it.id == 0 }
        upsertCategories(categories = toUpsert)
        val insertedIds = insertCategories(categories = toInsert).map { it.toInt() }.toMutableList()

        return toUpsert + toInsert.mapNotNull { category ->
            insertedIds.removeFirstOrNull()?.let { id ->
                category.copy(id = id)
            }
        }
    }

    @Delete
    suspend fun deleteCategories(categories: List<CategoryEntity>)

    @Transaction
    suspend fun deleteAndSaveCategories(
        toDelete: List<CategoryEntity>,
        toSave: List<CategoryEntity>
    ): List<CategoryEntity> {
        deleteCategories(categories = toDelete)
        return saveCategories(categories = toSave)
    }

    @Query("DELETE FROM category")
    suspend fun deleteAllCategories()

    @Query("SELECT * FROM category WHERE timestamp > :timestamp")
    suspend fun getCategoriesAfterTimestamp(timestamp: Long): List<CategoryEntity>

    @Query("SELECT * FROM category WHERE type = :type AND deleted = 0")
    suspend fun getCategoriesByType(type: Char): List<CategoryEntity>

    @Query("SELECT * FROM category WHERE deleted = 0")
    fun getAllCategoriesAsFlow(): Flow<List<CategoryEntity>>

}