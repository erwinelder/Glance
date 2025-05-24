package com.ataglance.walletglance.settings.domain.navigation

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

sealed interface SettingsScreens {

    @Serializable
    data object Start : SettingsScreens

    @Keep
    @Serializable
    data object Auth : SettingsScreens

    @Serializable
    data object SettingsHome : SettingsScreens

    @Serializable
    data object Accounts : SettingsScreens

    @Serializable
    data object Budgets : SettingsScreens

    @Serializable
    data object Categories : SettingsScreens

    @Serializable
    data object CategoryCollections : SettingsScreens

    @Serializable
    data object Personalisation : SettingsScreens

    @Serializable
    data object Notifications : SettingsScreens

    @Serializable
    data object Language : SettingsScreens

    @Serializable
    data object ResetData : SettingsScreens

}