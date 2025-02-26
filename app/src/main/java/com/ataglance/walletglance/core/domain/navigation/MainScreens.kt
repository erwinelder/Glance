package com.ataglance.walletglance.core.domain.navigation

import com.ataglance.walletglance.category.domain.model.CategoryType
import kotlinx.serialization.Serializable

sealed interface MainScreens {

    @Serializable
    data object Home : MainScreens

    @Serializable
    data object Records : MainScreens

    @Serializable
    data class CategoryStatistics(
        val parentCategoryId: Int? = null,
        val type: String = CategoryType.Expense.name
    ) : MainScreens

    @Serializable
    data object Budgets : MainScreens

    @Serializable
    data class BudgetStatistics(val id: Int) : MainScreens

    @Serializable
    data class RecordCreation(val recordNum: Int? = null) : MainScreens

    @Serializable
    data class TransferCreation(val recordNum: Int? = null) : MainScreens

    @Serializable
    data object Settings : MainScreens

    @Serializable
    data object FinishSetup : MainScreens

}