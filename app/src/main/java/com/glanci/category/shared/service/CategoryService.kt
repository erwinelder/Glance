package com.glanci.category.shared.service

import com.ataglance.walletglance.category.data.remote.model.CategoryCommandDto
import com.ataglance.walletglance.category.data.remote.model.CategoryQueryDto
import kotlinx.rpc.annotations.Rpc

@Rpc
interface CategoryService {

    suspend fun getUpdateTime(token: String): Long?

    suspend fun saveCategories(categories: List<CategoryCommandDto>, timestamp: Long, token: String)

    suspend fun getCategoriesAfterTimestamp(timestamp: Long, token: String): List<CategoryQueryDto>?

    suspend fun saveCategoriesAndGetAfterTimestamp(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<CategoryQueryDto>?

}