package com.ataglance.walletglance.budget.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface BudgetsSettingsScreens {

    @Serializable
    data object EditBudgets : BudgetsSettingsScreens

    @Serializable
    data object EditBudget : BudgetsSettingsScreens

}