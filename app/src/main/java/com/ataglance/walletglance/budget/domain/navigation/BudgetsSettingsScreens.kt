package com.ataglance.walletglance.budget.domain.navigation

import kotlinx.serialization.Serializable

sealed interface BudgetsSettingsScreens {

    @Serializable
    data object EditBudgets : BudgetsSettingsScreens

    @Serializable
    data object EditBudget : BudgetsSettingsScreens

}