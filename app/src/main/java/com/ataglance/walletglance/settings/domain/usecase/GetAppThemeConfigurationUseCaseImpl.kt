package com.ataglance.walletglance.settings.domain.usecase

import com.ataglance.walletglance.core.data.repository.SettingsRepository
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.utils.enumValueOrNull
import com.ataglance.walletglance.settings.domain.model.AppThemeConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class GetAppThemeConfigurationUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : GetAppThemeConfigurationUseCase {

    override fun getAsFlow(): Flow<AppThemeConfiguration> = flow {
        val useDeviceThemeFlow = settingsRepository.useDeviceTheme
        val chosenLightThemeFlow = settingsRepository.chosenLightTheme
        val chosenDarkThemeFlow = settingsRepository.chosenDarkTheme
        val lastChosenThemeFlow = settingsRepository.lastChosenTheme

        combine(
            useDeviceThemeFlow,
            chosenLightThemeFlow,
            chosenDarkThemeFlow,
            lastChosenThemeFlow
        ) { useDeviceTheme, chosenLightTheme, chosenDarkTheme, lastChosenTheme ->
            emit(
                value = AppThemeConfiguration(
                    useDeviceTheme = useDeviceTheme,
                    chosenLightTheme = enumValueOrNull<AppTheme>(chosenLightTheme) ?: AppTheme.LightDefault,
                    chosenDarkTheme = enumValueOrNull<AppTheme>(chosenDarkTheme) ?: AppTheme.DarkDefault,
                    lastChosenTheme = enumValueOrNull<AppTheme>(lastChosenTheme) ?: AppTheme.LightDefault
                )
            )
        }
    }

}