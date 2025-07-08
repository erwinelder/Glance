package com.ataglance.walletglance.navigation.data.remote.source

import com.ataglance.walletglance.navigation.data.remote.model.NavigationButtonDto

class NavigationButtonRemoteDataSourceImpl() : NavigationButtonRemoteDataSource {

    override suspend fun getUpdateTime(userId: Int): Long? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun synchronizeNavigationButtons(
        buttons: List<NavigationButtonDto>,
        timestamp: Long,
        userId: Int
    ): List<NavigationButtonDto>? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun synchronizeNavigationButtonsAndGetAfterTimestamp(
        buttons: List<NavigationButtonDto>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<NavigationButtonDto>? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun getNavigationButtonsAfterTimestamp(
        timestamp: Long,
        userId: Int
    ): List<NavigationButtonDto>? {
        // TODO("Not yet implemented")
        return null
    }

}