package com.ataglance.walletglance.category.data.local

import androidx.room.Transaction
import com.ataglance.walletglance.category.data.model.CategoryEntity
import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.TableUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow

class CategoryLocalDataSource(
    private val categoryDao: CategoryDao,
    updateTimeDao: TableUpdateTimeDao
) : BaseLocalDataSource<CategoryEntity>(
    dao = categoryDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.Category
) {

    @Transaction
    suspend fun deleteAllCategories(timestamp: Long) {
        categoryDao.deleteAllCategories()
        updateTime(timestamp)
    }

    fun getAllCategories(): Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

}