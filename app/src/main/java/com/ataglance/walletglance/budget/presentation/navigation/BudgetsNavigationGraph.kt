package com.ataglance.walletglance.budget.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.budget.domain.navigation.BudgetsSettingsScreens
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetScreenWrapper
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetsScreenWrapper
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens

fun NavGraphBuilder.budgetsGraph(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    isAppSetUp: Boolean
) {
    navigation<SettingsScreens.Budgets>(
        startDestination = BudgetsSettingsScreens.EditBudgets
    ) {
        composable<BudgetsSettingsScreens.EditBudgets> { backStack ->
            EditBudgetsScreenWrapper(
                screenPadding = screenPadding,
                backStack = backStack,
                navController = navController,
                navViewModel = navViewModel,
                isAppSetUp = isAppSetUp
            )
        }
        composable<BudgetsSettingsScreens.EditBudget> { backStack ->
            EditBudgetScreenWrapper(
                screenPadding = screenPadding,
                backStack = backStack,
                navController = navController
            )
        }
    }
}