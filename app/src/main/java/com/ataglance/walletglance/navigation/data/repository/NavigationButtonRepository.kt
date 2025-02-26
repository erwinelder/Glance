package com.ataglance.walletglance.navigation.data.repository

import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import kotlinx.coroutines.flow.Flow

interface NavigationButtonRepository {

    suspend fun upsertNavigationButtons(buttons: List<NavigationButtonEntity>)

    suspend fun deleteAllNavigationButtonsLocally()

    fun getAllNavigationButtonsFlow(): Flow<List<NavigationButtonEntity>>

}