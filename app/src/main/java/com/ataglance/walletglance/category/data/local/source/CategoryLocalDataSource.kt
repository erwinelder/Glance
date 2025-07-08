package com.ataglance.walletglance.category.data.local.source

import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun deleteUpdateTime()

    suspend fun saveCategories(
        categories: List<CategoryEntity>,
        timestamp: Long
    ): List<CategoryEntity>

    suspend fun deleteCategories(categories: List<CategoryEntity>)

    suspend fun deleteAllCategories()

    suspend fun deleteAndSaveCategories(
        toDelete: List<CategoryEntity>,
        toUpsert: List<CategoryEntity>,
        timestamp: Long
    ): List<CategoryEntity>

    suspend fun getCategoriesAfterTimestamp(timestamp: Long): List<CategoryEntity>

    fun getAllCategoriesAsFlow(): Flow<List<CategoryEntity>>

    suspend fun getAllCategories(): List<CategoryEntity>

    suspend fun getCategoriesByType(type: Char): List<CategoryEntity>

}