package com.ataglance.walletglance.navigation.data.remote.source

import com.ataglance.walletglance.navigation.data.remote.model.NavigationButtonDto

interface NavigationButtonRemoteDataSource {

    suspend fun getUpdateTime(userId: Int): Long?

    suspend fun synchronizeNavigationButtons(
        buttons: List<NavigationButtonDto>,
        timestamp: Long,
        userId: Int
    ): List<NavigationButtonDto>?

    suspend fun synchronizeNavigationButtonsAndGetAfterTimestamp(
        buttons: List<NavigationButtonDto>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<NavigationButtonDto>?

    suspend fun getNavigationButtonsAfterTimestamp(
        timestamp: Long,
        userId: Int
    ): List<NavigationButtonDto>?

}