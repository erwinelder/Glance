package com.ataglance.walletglance.category.data.local.source

import com.ataglance.walletglance.category.data.local.dao.CategoryLocalDao
import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow

class CategoryLocalDataSourceImpl(
    private val categoryDao: CategoryLocalDao,
    private val updateTimeDao: LocalUpdateTimeDao
) : CategoryLocalDataSource {

    override suspend fun getUpdateTime(): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.Category.name)
    }

    override suspend fun saveUpdateTime(timestamp: Long) {
        updateTimeDao.saveUpdateTime(tableName = TableName.Category.name, timestamp = timestamp)
    }

    override suspend fun upsertCategories(categories: List<CategoryEntity>, timestamp: Long) {
        categoryDao.upsertEntities(entities = categories)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun deleteAllCategories(timestamp: Long) {
        categoryDao.deleteAllCategories()
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun synchroniseCategories(
        categoriesToSync: EntitiesToSync<CategoryEntity>,
        timestamp: Long
    ) {
        categoryDao.deleteAndUpsertEntities(
            toDelete = categoriesToSync.toDelete,
            toUpsert = categoriesToSync.toUpsert
        )
        saveUpdateTime(timestamp = timestamp)
    }

    override fun getAllCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategories()
    }
}


fun getCategoryLocalDataSource(appDatabase: AppDatabase): CategoryLocalDataSource {
    return CategoryLocalDataSourceImpl(
        categoryDao = appDatabase.categoryDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}