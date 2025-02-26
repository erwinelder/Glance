package com.ataglance.walletglance.navigation.data.remote.source

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.dao.RemoteUpdateTimeDao
import com.ataglance.walletglance.navigation.data.remote.dao.NavigationButtonRemoteDao
import com.ataglance.walletglance.navigation.data.remote.model.NavigationButtonRemoteEntity

class NavigationButtonRemoteDataSourceImpl(
    private val navigationButtonDao: NavigationButtonRemoteDao,
    private val updateTimeDao: RemoteUpdateTimeDao
) : NavigationButtonRemoteDataSource {

    override suspend fun getUpdateTime(userId: String): Long? {
        return updateTimeDao.getUpdateTime(
            tableName = TableName.NavigationButton.name, userId = userId
        )
    }

    override suspend fun saveUpdateTime(timestamp: Long, userId: String) {
        updateTimeDao.saveUpdateTime(
            tableName = TableName.NavigationButton.name, timestamp = timestamp, userId = userId
        )
    }

    override suspend fun upsertNavigationButtons(
        buttons: List<NavigationButtonRemoteEntity>,
        timestamp: Long,
        userId: String
    ) {
        navigationButtonDao.upsertNavigationButtons(
            navigationButtons = buttons, userId = userId
        )
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun getNavigationButtonsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<NavigationButtonRemoteEntity> {
        return navigationButtonDao.getNavigationButtonsAfterTimestamp(
            timestamp = timestamp, userId = userId
        )
    }

}