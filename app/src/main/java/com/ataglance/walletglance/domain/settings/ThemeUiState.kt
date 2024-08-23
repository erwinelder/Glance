package com.ataglance.walletglance.domain.settings

data class ThemeUiState(
    val useDeviceTheme: Boolean,
    val chosenLightTheme: String,
    val chosenDarkTheme: String,
    val lastChosenTheme: String
)