package com.ataglance.walletglance.category.data.local

import androidx.room.Transaction
import com.ataglance.walletglance.category.data.model.CategoryEntity
import com.ataglance.walletglance.core.data.local.source.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName

class CategoryLocalDataSource(
    private val categoryDao: CategoryDao,
    updateTimeDao: LocalUpdateTimeDao
) : BaseLocalDataSource<CategoryEntity>(
    dao = categoryDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.Category
) {

    @Transaction
    suspend fun deleteAllCategories(timestamp: Long) {
        categoryDao.deleteAllCategories()
        updateLastModifiedTime(timestamp)
    }

}