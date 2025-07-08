package com.ataglance.walletglance.category.data.remote.source

import com.ataglance.walletglance.category.data.remote.model.CategoryCommandDto
import com.ataglance.walletglance.category.data.remote.model.CategoryQueryDto

interface CategoryRemoteDataSource {

    suspend fun getUpdateTime(userId: Int): Long?

    suspend fun synchronizeCategories(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        userId: Int
    ): List<CategoryQueryDto>?

    suspend fun synchronizeCategoriesAndGetAfterTimestamp(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<CategoryQueryDto>?

    suspend fun getCategoriesAfterTimestamp(timestamp: Long, userId: Int): List<CategoryQueryDto>

}
