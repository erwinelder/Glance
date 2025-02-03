package com.ataglance.walletglance.category.data.remote.source

import com.ataglance.walletglance.category.data.remote.dao.CategoryRemoteDao
import com.ataglance.walletglance.category.data.remote.model.CategoryRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSynchronise
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
        categoryDao.upsertEntities(entities = categories, userId = userId)
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun synchroniseCategories(
        categoriesToSync: EntitiesToSynchronise<CategoryRemoteEntity>,
        timestamp: Long,
        userId: String
    ) {
        categoryDao.synchroniseEntities(entitiesToSync = categoriesToSync, userId = userId)
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun getCategoriesAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSynchronise<CategoryRemoteEntity> {
        return categoryDao.getEntitiesAfterTimestamp(timestamp = timestamp, userId = userId)
    }

}