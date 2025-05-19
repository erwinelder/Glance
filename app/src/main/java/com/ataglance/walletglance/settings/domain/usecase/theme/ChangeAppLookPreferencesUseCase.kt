package com.ataglance.walletglance.settings.domain.usecase.theme

import com.ataglance.walletglance.core.domain.app.AppTheme

interface ChangeAppLookPreferencesUseCase {

    suspend fun saveLightThemePreference(theme: AppTheme)

    suspend fun saveDarkThemePreference(theme: AppTheme)

    suspend fun saveUseDeviceThemePreference(value: Boolean)

}