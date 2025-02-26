package com.ataglance.walletglance.settings.domain.usecase

import com.ataglance.walletglance.settings.data.repository.SettingsRepository
import com.ataglance.walletglance.core.domain.app.AppTheme
import kotlinx.coroutines.flow.firstOrNull

class ChangeAppLookPreferencesUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : ChangeAppLookPreferencesUseCase {


    override suspend fun saveLightThemePreference(theme: AppTheme) {
        val useDeviceTheme = settingsRepository.useDeviceTheme.firstOrNull()

        /**
         * Now it setting up useDeviceTheme to false when any theme is chosen (when user clicks on the
         * preview theme element on the SetupAppearanceScreen or settings AppearanceScreen) because
         * currently there are only 2 themes (Light and Dark) to choose between. When there are more
         * themes of light or dark versions, it is necessary to remove this block of code that unsets
         * useDeviceTheme preference so user can specify which theme they want to use as light and
         * which as a dark one.
         */
        if (useDeviceTheme == true) {
            settingsRepository.saveUseDeviceThemePreference(false)
        }

        settingsRepository.saveChosenLightThemePreference(theme.name)
        settingsRepository.saveLastChosenThemePreference(theme.name)
    }

    override suspend fun saveDarkThemePreference(theme: AppTheme) {
        val useDeviceTheme = settingsRepository.useDeviceTheme.firstOrNull()

        /**
         * Now it setting up useDeviceTheme to false when any theme is chosen (when user clicks on the
         * preview theme element on the SetupAppearanceScreen or settings AppearanceScreen) because
         * currently there are only 2 themes (Light and Dark) to choose between. When there are more
         * themes of light or dark versions, it is necessary to remove this block of code that unsets
         * useDeviceTheme preference so user can specify which theme they want to use as light and
         * which as a dark one.
         */
        if (useDeviceTheme == true) {
            settingsRepository.saveUseDeviceThemePreference(false)
        }

        settingsRepository.saveChosenDarkThemePreference(theme.name)
        settingsRepository.saveLastChosenThemePreference(theme.name)
    }

    override suspend fun saveUseDeviceThemePreference(value: Boolean) {
        settingsRepository.saveUseDeviceThemePreference(value)
    }

}