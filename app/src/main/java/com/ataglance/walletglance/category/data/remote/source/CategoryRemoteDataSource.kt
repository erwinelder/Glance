package com.ataglance.walletglance.category.data.remote.source

import com.ataglance.walletglance.category.data.remote.model.CategoryRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSync

interface CategoryRemoteDataSource {

    suspend fun getUpdateTime(userId: String): Long?

    suspend fun saveUpdateTime(timestamp: Long, userId: String)

    suspend fun upsertCategories(
        categories: List<CategoryRemoteEntity>,
        timestamp: Long,
        userId: String
    )

    suspend fun synchroniseCategories(
        categoriesToSync: EntitiesToSync<CategoryRemoteEntity>,
        timestamp: Long,
        userId: String
    )

    suspend fun getCategoriesAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<CategoryRemoteEntity>

}
