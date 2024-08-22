package com.ataglance.walletglance.presentation.theme.navigation.screens

import kotlinx.serialization.Serializable

sealed interface MainScreens {

    @Serializable
    data object Home : MainScreens

    @Serializable
    data object Records : MainScreens

    @Serializable
    data class CategoryStatistics(val parentCategoryId: Int) : MainScreens

    @Serializable
    data object Budgets : MainScreens

    @Serializable
    data class BudgetStatistics(val id: Int) : MainScreens

    @Serializable
    data class MakeRecord(val status: String, val recordNum: Int) : MainScreens

    @Serializable
    data class MakeTransfer(val status: String, val recordNum: Int) : MainScreens

    @Serializable
    data object Settings : MainScreens

    @Serializable
    data object FinishSetup : MainScreens

}