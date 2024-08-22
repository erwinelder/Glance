package com.ataglance.walletglance.data.app

import com.ataglance.walletglance.presentation.theme.navigation.screens.MainScreens
import com.ataglance.walletglance.presentation.theme.navigation.screens.SettingsScreens

data class AppUiSettings(
    val isSetUp: Boolean = false,
    val startMainDestination: MainScreens = MainScreens.Home,
    val startSettingsDestination: SettingsScreens = SettingsScreens.Start,
    val langCode: String = AppLanguage.English.languageCode,
    val appTheme: AppTheme? = null,
    val lastRecordNum: Int = 0,
) {
    fun nextRecordNum(): Int {
        return lastRecordNum + 1
    }
}