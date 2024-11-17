package com.ataglance.walletglance.core.domain.app

import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.settings.navigation.SettingsScreens

data class AppConfiguration(
    val isSetUp: Boolean = false,
    val isSignedIn: Boolean = false,
    val mainStartDestination: MainScreens = MainScreens.Home,
    val startSettingsDestination: SettingsScreens = SettingsScreens.Start,
    val langCode: String = AppLanguage.English.languageCode,
    val appTheme: AppTheme? = null,
    val lastRecordNum: Int = 0,
) {
    fun nextRecordNum(): Int {
        return lastRecordNum + 1
    }
}