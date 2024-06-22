package com.ataglance.walletglance.ui.theme.navigation.screens

import kotlinx.serialization.Serializable

sealed interface SettingsScreens {

    @Serializable
    data object Start : SettingsScreens

    @Serializable
    data object SettingsHome : SettingsScreens

    @Serializable
    data object Accounts : SettingsScreens

    @Serializable
    data object Categories : SettingsScreens

    @Serializable
    data object CategoryCollections : SettingsScreens

    @Serializable
    data object Appearance : SettingsScreens

    @Serializable
    data object Language : SettingsScreens

    @Serializable
    data object ResetData : SettingsScreens

}