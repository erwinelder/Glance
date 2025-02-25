package com.ataglance.walletglance.core.domain.app

import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens

data class AppConfiguration(
    val isSetUp: Boolean = false,
    val isSignedIn: Boolean = false,
    val mainStartDestination: MainScreens = MainScreens.Home,
    val settingsStartDestination: SettingsScreens = SettingsScreens.Start,
    val langCode: String = AppLanguage.English.languageCode,
    val appTheme: AppTheme? = null
)
