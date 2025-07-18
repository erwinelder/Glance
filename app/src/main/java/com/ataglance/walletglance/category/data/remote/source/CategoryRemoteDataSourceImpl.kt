package com.ataglance.walletglance.category.data.remote.source

import android.util.Log
import com.ataglance.walletglance.category.data.remote.model.CategoryCommandDto
import com.ataglance.walletglance.category.data.remote.model.CategoryQueryDto
import com.glanci.category.shared.service.CategoryService
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class CategoryRemoteDataSourceImpl(
    private val service: CategoryService
) : CategoryRemoteDataSource {

    constructor(client: KtorRpcClient) : this(service = client.withService<CategoryService>())


    override suspend fun getUpdateTime(token: String): Long? {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrNull().also { timestamp ->
            if (timestamp != null) {
                Log.d("CategoryRemoteDataSourceImpl", "getUpdateTime:" +
                        "received update time $timestamp")
            } else {
                Log.w("CategoryRemoteDataSourceImpl", "getUpdateTime:" +
                        "no update time received")
            }
        }
    }

    override suspend fun synchronizeCategories(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        token: String
    ): Boolean {
        return runCatching {
            service.saveCategories(categories = categories, timestamp = timestamp, token = token)
        }.isSuccess.also { success ->
            if (success) {
                Log.d("CategoryRemoteDataSourceImpl", "synchronizeCategories: " +
                        "synchronized ${categories.size} categories at timestamp $timestamp")
            } else {
                Log.e("CategoryRemoteDataSourceImpl", "synchronizeCategories: " +
                        "failed to synchronize ${categories.size} categories at timestamp $timestamp")
            }
        }
    }

    override suspend fun getCategoriesAfterTimestamp(
        timestamp: Long,
        token: String
    ): List<CategoryQueryDto>? {
        return runCatching {
            service.getCategoriesAfterTimestamp(timestamp = timestamp, token = token)
        }.getOrNull().also { categories ->
            categories?.forEach {
                Log.d("CategoryRemoteDataSourceImpl", "getCategoriesAfterTimestamp:" +
                        "received category: id = ${it.id}, name = ${it.name}")
            }
        }
    }

    override suspend fun synchronizeCategoriesAndGetAfterTimestamp(
        categories: List<CategoryCommandDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<CategoryQueryDto>? {
        return runCatching {
            service.saveCategoriesAndGetAfterTimestamp(
                categories = categories,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrNull().also { categories ->
            Log.d("CategoryRemoteDataSourceImpl", "synchronizeCategoriesAndGetAfterTimestamp:" +
                    "synchronized ${categories?.size ?: 0} categories at timestamp $timestamp" +
                    " with local timestamp $localTimestamp")
            categories?.forEach {
                Log.d("CategoryRemoteDataSourceImpl", "synchronizeCategoriesAndGetAfterTimestamp:" +
                        "received category: id = ${it.id}, name = ${it.name}")
            }
        }
    }

}