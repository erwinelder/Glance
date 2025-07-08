package com.ataglance.walletglance.navigation.data.local.source

import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.navigation.data.local.dao.NavigationButtonLocalDao
import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import kotlinx.coroutines.flow.Flow

class NavigationButtonLocalDataSourceImpl(
    private val navigationButtonDao: NavigationButtonLocalDao,
    private val updateTimeDao: LocalUpdateTimeDao
) : NavigationButtonLocalDataSource {

    override suspend fun getUpdateTime(): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.NavigationButton.name)
    }

    override suspend fun saveUpdateTime(timestamp: Long) {
        updateTimeDao.saveUpdateTime(
            tableName = TableName.NavigationButton.name, timestamp = timestamp
        )
    }

    override suspend fun deleteUpdateTime() {
        updateTimeDao.deleteUpdateTime(tableName = TableName.NavigationButton.name)
    }

    override suspend fun upsertNavigationButtons(
        buttons: List<NavigationButtonEntity>,
        timestamp: Long
    ) {
        navigationButtonDao.upsertButtons(buttons = buttons)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun deleteNavigationButtons(buttons: List<NavigationButtonEntity>) {
        navigationButtonDao.deleteButtons(buttons = buttons)
    }

    override suspend fun deleteAllNavigationButtons() {
        navigationButtonDao.deleteAllNavigationButtons()
        deleteUpdateTime()
    }

    override suspend fun deleteAndUpsertNavigationButtons(
        toDelete: List<NavigationButtonEntity>,
        toUpsert: List<NavigationButtonEntity>,
        timestamp: Long
    ) {
        navigationButtonDao.deleteAndUpsertButtons(toDelete = toDelete, toUpsert = toUpsert)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun getNavigationButtonsAfterTimestamp(
        timestamp: Long
    ): List<NavigationButtonEntity> {
        return navigationButtonDao.getNavigationButtonsAfterTimestamp(timestamp = timestamp)
    }

    override fun getAllNavigationButtonsAsFlow(): Flow<List<NavigationButtonEntity>> {
        return navigationButtonDao.getAllNavigationButtonsAsFlow()
    }

}

fun getNavigationButtonLocalDataSource(appDatabase: AppDatabase): NavigationButtonLocalDataSource {
    return NavigationButtonLocalDataSourceImpl(
        navigationButtonDao = appDatabase.navigationButtonDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}