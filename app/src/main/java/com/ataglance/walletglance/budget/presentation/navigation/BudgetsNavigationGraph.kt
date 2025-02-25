package com.ataglance.walletglance.budget.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.navigation.BudgetsSettingsScreens
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetScreen
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetsScreen
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetsViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import kotlinx.coroutines.launch

fun NavGraphBuilder.budgetsGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    navViewModel: NavigationViewModel,
    isAppSetUp: Boolean
) {
    navigation<SettingsScreens.Budgets>(
        startDestination = BudgetsSettingsScreens.EditBudgets
    ) {
        composable<BudgetsSettingsScreens.EditBudgets> { backStack ->
            val budgetsViewModel = backStack.sharedKoinNavViewModel<EditBudgetsViewModel>(navController)
            val budgetViewModel = backStack.sharedKoinNavViewModel<EditBudgetViewModel>(navController)

            val budgetsByType by budgetsViewModel.budgetsByType.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            EditBudgetsScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = isAppSetUp,
                budgetsByType = budgetsByType,
                onNavigateToEditBudgetScreen = { budget: Budget? ->
                    budgetViewModel.applyBudget(budget)
                    navViewModel.navigateToScreen(navController, BudgetsSettingsScreens.EditBudget)
                },
                onSaveBudgetsButton = {
                    coroutineScope.launch {
                        budgetsViewModel.saveBudgets()
                        if (isAppSetUp) {
                            navController.popBackStack()
                        } else {
                            budgetsViewModel.preFinishSetup()
                        }
                    }
                }
            )
        }
        composable<BudgetsSettingsScreens.EditBudget> { backStack ->
            val budgetsViewModel = backStack.sharedKoinNavViewModel<EditBudgetsViewModel>(navController)
            val budgetViewModel = backStack.sharedKoinNavViewModel<EditBudgetViewModel>(navController)

            val budget by budgetViewModel.budget.collectAsStateWithLifecycle()

            EditBudgetScreen(
                scaffoldPadding = scaffoldPadding,
                budget = budget,
                accountList = budgetViewModel.accounts,
                groupedCategoriesByType = budgetViewModel.groupedCategoriesByType,
                onNameChange = budgetViewModel::changeName,
                onCategoryChange = budgetViewModel::changeCategory,
                onAmountLimitChange = budgetViewModel::changeAmountLimit,
                onRepeatingPeriodChange = budgetViewModel::changeRepeatingPeriod,
                onLinkAccount = budgetViewModel::linkWithAccount,
                onUnlinkAccount = budgetViewModel::unlinkWithAccount,
                onDeleteButton = {
                    budgetsViewModel.deleteBudget(
                        id = budget.id, repeatingPeriod = budget.currRepeatingPeriod
                    )
                    navController.popBackStack()
                },
                onSaveButton = {
                    budgetsViewModel.applyBudget(budgetDraft = budgetViewModel.getBudgetDraft())
                    navController.popBackStack()
                }
            )
        }
    }
}