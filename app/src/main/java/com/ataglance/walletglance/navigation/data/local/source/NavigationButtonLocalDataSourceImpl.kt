package com.ataglance.walletglance.navigation.data.local.source

import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.EntitiesToSync
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

    override suspend fun upsertNavigationButtons(
        buttons: List<NavigationButtonEntity>,
        timestamp: Long
    ) {
        navigationButtonDao.upsertButtons(buttons = buttons)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun synchroniseNavigationButtons(
        buttonsToSync: EntitiesToSync<NavigationButtonEntity>,
        timestamp: Long
    ) {
        navigationButtonDao.deleteAndUpsertButtons(
            toDelete = buttonsToSync.toDelete,
            toUpsert = buttonsToSync.toUpsert
        )
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun deleteAllNavigationButtons(timestamp: Long) {
        navigationButtonDao.deleteAllNavigationButtons()
        saveUpdateTime(timestamp = timestamp)
    }

    override fun getAllNavigationButtons(): Flow<List<NavigationButtonEntity>> {
        return navigationButtonDao.getAllNavigationButtons()
    }

}

fun getNavigationButtonLocalDataSource(appDatabase: AppDatabase): NavigationButtonLocalDataSource {
    return NavigationButtonLocalDataSourceImpl(
        navigationButtonDao = appDatabase.navigationButtonDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}