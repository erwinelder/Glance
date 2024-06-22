package com.ataglance.walletglance.ui.theme.navigation.screens

import kotlinx.serialization.Serializable

sealed interface AccountsSettingsScreens {

    @Serializable
    data object EditAccounts : AccountsSettingsScreens

    @Serializable
    data class EditAccount(val orderNum: Int) : AccountsSettingsScreens

    @Serializable
    data class EditAccountCurrency(val currency: String) : AccountsSettingsScreens

}