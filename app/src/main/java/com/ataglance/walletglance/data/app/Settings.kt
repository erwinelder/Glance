package com.ataglance.walletglance.data.app

data class Settings(
    val language: String,
    val useDeviceTheme: Boolean,
    val chosenLightTheme: String,
    val chosenDarkTheme: String,
    val lastChosenTheme: String
)