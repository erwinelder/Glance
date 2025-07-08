package com.ataglance.walletglance.navigation.data.repository

import com.ataglance.walletglance.navigation.data.model.NavigationButtonDataModel
import kotlinx.coroutines.flow.Flow

interface NavigationButtonRepository {

    suspend fun upsertNavigationButtons(buttons: List<NavigationButtonDataModel>)

    suspend fun deleteAllNavigationButtonsLocally()

    fun getAllNavigationButtonsAsFlow(): Flow<List<NavigationButtonDataModel>>

}