package com.ataglance.walletglance.data

data class Settings(
    val language: String,
    val useDeviceTheme: Boolean,
    val chosenLightTheme: String,
    val chosenDarkTheme: String,
    val lastChosenTheme: String
)