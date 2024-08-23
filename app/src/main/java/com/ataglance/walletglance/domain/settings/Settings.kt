package com.ataglance.walletglance.domain.settings

data class Settings(
    val language: String,
    val useDeviceTheme: Boolean,
    val chosenLightTheme: String,
    val chosenDarkTheme: String,
    val lastChosenTheme: String
)