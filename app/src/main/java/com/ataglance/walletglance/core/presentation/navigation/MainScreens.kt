package com.ataglance.walletglance.core.presentation.navigation

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
    data class RecordCreation(val recordNum: Int? = null) : MainScreens

    @Serializable
    data class TransferCreation(val isNew: Boolean, val recordNum: Int) : MainScreens

    @Serializable
    data object Settings : MainScreens

    @Serializable
    data object FinishSetup : MainScreens

}