package com.ataglance.walletglance.navigation.data.repository

import com.ataglance.walletglance.core.utils.getNowDateTimeLong
import com.ataglance.walletglance.navigation.data.local.NavigationButtonLocalDataSource
import com.ataglance.walletglance.navigation.data.remote.NavigationButtonRemoteDataSource

class NavigationButtonRepositoryImpl(
    override val localSource: NavigationButtonLocalDataSource,
    override val remoteSource: NavigationButtonRemoteDataSource?
) : NavigationButtonRepository {

    override suspend fun deleteAllEntities(
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        val timestamp = getNowDateTimeLong()
        localSource.deleteAllNavigationButtons(timestamp)
        remoteSource?.deleteAllEntities(
            timestamp = timestamp,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
    }
}