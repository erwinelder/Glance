package com.ataglance.walletglance.presentation.ui.navigation.screens

import kotlinx.serialization.Serializable

sealed interface BudgetsSettingsScreens {

    @Serializable
    data object EditBudgets : BudgetsSettingsScreens

    @Serializable
    data object EditBudget : BudgetsSettingsScreens

}