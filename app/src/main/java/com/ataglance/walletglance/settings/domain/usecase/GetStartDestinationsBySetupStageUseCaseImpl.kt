package com.ataglance.walletglance.settings.domain.usecase

import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.settings.data.repository.SettingsRepository
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class GetStartDestinationsBySetupStageUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : GetStartDestinationsBySetupStageUseCase {

    override fun getFlow(): Flow<Pair<MainScreens, SettingsScreens>> = flow {
        settingsRepository.setupStage.collect { setupStage ->
            val mainStartDestination = when(setupStage) {
                1 -> MainScreens.Home
                0 -> MainScreens.Settings
                else -> MainScreens.FinishSetup
            }
            val settingsStartDestination = when (setupStage) {
                1 -> SettingsScreens.SettingsHome
                else -> SettingsScreens.Start
            }
            emit(mainStartDestination to settingsStartDestination)
        }
    }

    override suspend fun get(): Pair<MainScreens, SettingsScreens> {
        val setupStage = settingsRepository.setupStage.firstOrNull()

        val mainStartDestination = when(setupStage) {
            1 -> MainScreens.Home
            0 -> MainScreens.Settings
            else -> MainScreens.FinishSetup
        }
        val settingsStartDestination = when (setupStage) {
            1 -> SettingsScreens.SettingsHome
            else -> SettingsScreens.Start
        }

        return mainStartDestination to settingsStartDestination
    }

}