package com.ataglance.walletglance.navigation.data.remote.source

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.navigation.data.remote.model.NavigationButtonRemoteEntity

interface NavigationButtonRemoteDataSource {

    suspend fun getUpdateTime(userId: String): Long?

    suspend fun saveUpdateTime(timestamp: Long, userId: String)

    suspend fun upsertNavigationButtons(
        buttons: List<NavigationButtonRemoteEntity>,
        timestamp: Long,
        userId: String
    )

    suspend fun getNavigationButtonsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<NavigationButtonRemoteEntity>

}