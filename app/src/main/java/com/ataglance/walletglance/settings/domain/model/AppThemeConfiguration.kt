package com.ataglance.walletglance.settings.domain.model

import com.ataglance.walletglance.core.domain.app.AppTheme

data class AppThemeConfiguration(
    val useDeviceTheme: Boolean = true,
    val chosenLightTheme: AppTheme = AppTheme.LightDefault,
    val chosenDarkTheme: AppTheme = AppTheme.DarkDefault,
    val lastChosenTheme: AppTheme = AppTheme.LightDefault
)