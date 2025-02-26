package com.ataglance.walletglance.navigation.data.local.source

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import kotlinx.coroutines.flow.Flow

interface NavigationButtonLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun upsertNavigationButtons(buttons: List<NavigationButtonEntity>, timestamp: Long)

    suspend fun synchroniseNavigationButtons(
        buttonsToSync: EntitiesToSync<NavigationButtonEntity>,
        timestamp: Long
    )

    suspend fun deleteAllNavigationButtons(timestamp: Long)

    fun getAllNavigationButtons(): Flow<List<NavigationButtonEntity>>

}