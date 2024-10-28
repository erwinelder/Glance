package com.ataglance.walletglance.categoryCollection.data.local

import androidx.room.Transaction
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionEntity
import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.TableUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName

class CategoryCollectionLocalDataSource(
    private val categoryCollectionDao: CategoryCollectionDao,
    updateTimeDao: TableUpdateTimeDao
) : BaseLocalDataSource<CategoryCollectionEntity>(
    dao = categoryCollectionDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.CategoryCollection
) {

    @Transaction
    suspend fun deleteAllCollections(timestamp: Long) {
        categoryCollectionDao.deleteAllCollections()
        updateLastModifiedTime(timestamp)
    }

}