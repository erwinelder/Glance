package com.ataglance.walletglance.category.data.remote.source

import com.ataglance.walletglance.category.data.remote.model.CategoryCommandDto
import com.ataglance.walletglance.category.data.remote.model.CategoryQueryDto

class CategoryRemoteDataSourceImpl() : CategoryRemoteDataSource {

    override suspend fun getUpdateTime(userId: Int): Long? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun synchronizeCategories(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        userId: Int
    ): Boolean {
        // TODO("Not yet implemented")
        return false
    }

    override suspend fun synchronizeCategoriesAndGetAfterTimestamp(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<CategoryQueryDto>? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun getCategoriesAfterTimestamp(
        timestamp: Long,
        userId: Int
    ): List<CategoryQueryDto> {
        // TODO("Not yet implemented")
        return emptyList()
    }

}