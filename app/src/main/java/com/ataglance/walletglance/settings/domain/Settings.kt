package com.ataglance.walletglance.settings.domain

data class Settings(
    val language: String,
    val useDeviceTheme: Boolean,
    val chosenLightTheme: String,
    val chosenDarkTheme: String,
    val lastChosenTheme: String
)