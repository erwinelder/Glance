package com.ataglance.walletglance.category.data.local.source

import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import kotlinx.coroutines.flow.Flow

interface CategoryLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun upsertCategories(categories: List<CategoryEntity>, timestamp: Long)

    suspend fun deleteAllCategories(timestamp: Long)

    suspend fun synchroniseCategories(
        categoriesToSync: EntitiesToSync<CategoryEntity>,
        timestamp: Long
    )

    fun getAllCategories(): Flow<List<CategoryEntity>>

    suspend fun getCategoriesByType(type: Char): List<CategoryEntity>

}