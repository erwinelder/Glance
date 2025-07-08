package com.ataglance.walletglance.navigation.data.local.source

import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import kotlinx.coroutines.flow.Flow

interface NavigationButtonLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun deleteUpdateTime()

    suspend fun upsertNavigationButtons(buttons: List<NavigationButtonEntity>, timestamp: Long)

    suspend fun deleteNavigationButtons(buttons: List<NavigationButtonEntity>)

    suspend fun deleteAllNavigationButtons()

    suspend fun deleteAndUpsertNavigationButtons(
        toDelete: List<NavigationButtonEntity>,
        toUpsert: List<NavigationButtonEntity>,
        timestamp: Long
    )

    suspend fun getNavigationButtonsAfterTimestamp(timestamp: Long): List<NavigationButtonEntity>

    fun getAllNavigationButtonsAsFlow(): Flow<List<NavigationButtonEntity>>

}