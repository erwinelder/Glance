package com.ataglance.walletglance.category.data.local.source

import com.ataglance.walletglance.category.data.local.dao.CategoryLocalDao
import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

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

    override suspend fun deleteUpdateTime() {
        updateTimeDao.deleteUpdateTime(tableName = TableName.Category.name)
    }

    override suspend fun saveCategories(
        categories: List<CategoryEntity>,
        timestamp: Long
    ): List<CategoryEntity> {
        return categoryDao.saveCategories(categories = categories).also {
            saveUpdateTime(timestamp = timestamp)
        }
    }

    override suspend fun deleteCategories(categories: List<CategoryEntity>) {
        categoryDao.deleteCategories(categories = categories)
    }

    override suspend fun deleteAllCategories() {
        categoryDao.deleteAllCategories()
        deleteUpdateTime()
    }

    override suspend fun deleteAndSaveCategories(
        toDelete: List<CategoryEntity>,
        toUpsert: List<CategoryEntity>,
        timestamp: Long
    ): List<CategoryEntity> {
        return categoryDao.deleteAndSaveCategories(toDelete = toDelete, toSave = toUpsert)
            .also { saveUpdateTime(timestamp = timestamp) }
    }

    override suspend fun getCategoriesAfterTimestamp(timestamp: Long): List<CategoryEntity> {
        return categoryDao.getCategoriesAfterTimestamp(timestamp = timestamp)
    }

    override fun getAllCategoriesAsFlow(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategoriesAsFlow()
    }

    override suspend fun getAllCategories(): List<CategoryEntity> {
        return categoryDao.getAllCategoriesAsFlow().firstOrNull().orEmpty()
    }

    override suspend fun getCategoriesByType(type: Char): List<CategoryEntity> {
        return categoryDao.getCategoriesByType(type = type)
    }

}

fun getCategoryLocalDataSource(appDatabase: AppDatabase): CategoryLocalDataSource {
    return CategoryLocalDataSourceImpl(
        categoryDao = appDatabase.categoryDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}
