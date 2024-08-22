package com.ataglance.walletglance.presentation.theme.navigation.screens

import kotlinx.serialization.Serializable

sealed interface AccountsSettingsScreens {

    @Serializable
    data object EditAccounts : AccountsSettingsScreens

    @Serializable
    data object EditAccount : AccountsSettingsScreens

    @Serializable
    data class EditAccountCurrency(val currency: String) : AccountsSettingsScreens

}