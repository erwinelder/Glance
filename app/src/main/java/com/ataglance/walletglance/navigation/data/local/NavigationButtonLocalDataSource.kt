package com.ataglance.walletglance.navigation.data.local

import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.TableUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.navigation.data.model.NavigationButtonEntity
import kotlinx.coroutines.flow.Flow

class NavigationButtonLocalDataSource(
    private val navigationButtonDao: NavigationButtonDao,
    updateTimeDao: TableUpdateTimeDao
) : BaseLocalDataSource<NavigationButtonEntity>(
    dao = navigationButtonDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.NavigationButton
) {

    fun getAllNavigationButtons(): Flow<List<NavigationButtonEntity>> =
        navigationButtonDao.getAllNavigationButtons()

}