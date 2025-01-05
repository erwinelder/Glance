package com.ataglance.walletglance.navigation.data.repository

import com.ataglance.walletglance.core.utils.getNowDateTimeLong
import com.ataglance.walletglance.navigation.data.local.NavigationButtonLocalDataSource
import com.ataglance.walletglance.navigation.data.remote.NavigationButtonRemoteDataSource

class NavigationButtonRepositoryImpl(
    override val localSource: NavigationButtonLocalDataSource,
    override val remoteSource: NavigationButtonRemoteDataSource?
) : NavigationButtonRepository {

    override suspend fun deleteAllEntities() {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllNavigationButtons(timestamp)
        remoteSource?.deleteAllEntities(timestamp = timestamp)
    }

    override suspend fun deleteAllEntitiesLocally() {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllNavigationButtons(timestamp = timestamp)
    }

}