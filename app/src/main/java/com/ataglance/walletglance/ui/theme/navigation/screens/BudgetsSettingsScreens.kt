package com.ataglance.walletglance.ui.theme.navigation.screens

import kotlinx.serialization.Serializable

sealed interface BudgetsSettingsScreens {

    @Serializable
    data object EditBudgets : BudgetsSettingsScreens

    @Serializable
    data object EditBudget : BudgetsSettingsScreens

}