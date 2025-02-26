package com.ataglance.walletglance.settings.domain.usecase

import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.utils.enumValueOrNull
import com.ataglance.walletglance.settings.data.repository.SettingsRepository
import com.ataglance.walletglance.settings.domain.model.AppThemeConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAppThemeConfigurationUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : GetAppThemeConfigurationUseCase {

    override fun getFlow(): Flow<AppThemeConfiguration> = combine(
        settingsRepository.useDeviceTheme,
        settingsRepository.chosenLightTheme,
        settingsRepository.chosenDarkTheme,
        settingsRepository.lastChosenTheme
    ) { useDeviceTheme, chosenLightTheme, chosenDarkTheme, lastChosenTheme ->
        AppThemeConfiguration(
            useDeviceTheme = useDeviceTheme,
            chosenLightTheme = enumValueOrNull<AppTheme>(chosenLightTheme) ?: AppTheme.LightDefault,
            chosenDarkTheme = enumValueOrNull<AppTheme>(chosenDarkTheme) ?: AppTheme.DarkDefault,
            lastChosenTheme = enumValueOrNull<AppTheme>(lastChosenTheme) ?: AppTheme.LightDefault
        )
    }

}