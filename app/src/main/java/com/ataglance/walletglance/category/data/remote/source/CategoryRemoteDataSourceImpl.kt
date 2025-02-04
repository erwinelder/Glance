package com.ataglance.walletglance.category.data.remote.source

import com.ataglance.walletglance.category.data.remote.dao.CategoryRemoteDao
import com.ataglance.walletglance.category.data.remote.model.CategoryRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.dao.RemoteUpdateTimeDao

class CategoryRemoteDataSourceImpl(
    private val categoryDao: CategoryRemoteDao,
    private val updateTimeDao: RemoteUpdateTimeDao
) : CategoryRemoteDataSource {

    override suspend fun getUpdateTime(userId: String): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.Category.name, userId = userId)
    }

    override suspend fun saveUpdateTime(timestamp: Long, userId: String) {
        updateTimeDao.saveUpdateTime(
            tableName = TableName.Category.name, timestamp = timestamp, userId = userId
        )
    }

    override suspend fun upsertCategories(
        categories: List<CategoryRemoteEntity>,
        timestamp: Long,
        userId: String
    ) {
        categoryDao.upsertCategories(categories = categories, userId = userId)
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun synchroniseCategories(
        categoriesToSync: EntitiesToSync<CategoryRemoteEntity>,
        timestamp: Long,
        userId: String
    ) {
        categoryDao.synchroniseCategories(categoriesToSync = categoriesToSync, userId = userId)
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun getCategoriesAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<CategoryRemoteEntity> {
        return categoryDao.getCategoriesAfterTimestamp(timestamp = timestamp, userId = userId)
    }

}