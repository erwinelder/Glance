package com.ataglance.walletglance.settings.domain.usecase

import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.settings.navigation.SettingsScreens
import kotlinx.coroutines.flow.Flow

interface GetStartDestinationsBySetupStageUseCase {

    fun getFlow(): Flow<Pair<MainScreens, SettingsScreens>>

    suspend fun get(): Pair<MainScreens, SettingsScreens>

}