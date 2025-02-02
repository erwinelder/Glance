package com.ataglance.walletglance.navigation.data.local

import androidx.room.Transaction
import com.ataglance.walletglance.core.data.local.source.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.navigation.data.model.NavigationButtonEntity

class NavigationButtonLocalDataSource(
    private val navigationButtonDao: NavigationButtonDao,
    updateTimeDao: LocalUpdateTimeDao
) : BaseLocalDataSource<NavigationButtonEntity>(
    dao = navigationButtonDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.NavigationButton
) {

    @Transaction
    suspend fun deleteAllNavigationButtons(timestamp: Long) {
        navigationButtonDao.deleteAllNavigationButtons()
        updateLastModifiedTime(timestamp)
    }

}