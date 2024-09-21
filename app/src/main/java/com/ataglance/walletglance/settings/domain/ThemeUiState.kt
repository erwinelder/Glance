package com.ataglance.walletglance.settings.domain

data class ThemeUiState(
    val useDeviceTheme: Boolean,
    val chosenLightTheme: String,
    val chosenDarkTheme: String,
    val lastChosenTheme: String
)