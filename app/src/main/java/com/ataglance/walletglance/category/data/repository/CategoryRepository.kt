package com.ataglance.walletglance.category.data.repository

import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun upsertCategories(categories: List<CategoryEntity>)

    suspend fun deleteAndUpsertCategories(
        toDelete: List<CategoryEntity>,
        toUpsert: List<CategoryEntity>
    )

    suspend fun deleteAllCategoriesLocally()

    fun getAllCategoriesFlow(): Flow<List<CategoryEntity>>

    suspend fun getAllCategories(): List<CategoryEntity>

    suspend fun getCategoriesByType(type: Char): List<CategoryEntity>

}