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
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetScreen
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetsScreen
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetsViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.navigation.SettingsScreens
import kotlinx.coroutines.launch

fun NavGraphBuilder.budgetsGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    navViewModel: NavigationViewModel,
    appViewModel: AppViewModel,
    isAppSetUp: Boolean
) {
    navigation<SettingsScreens.Budgets>(
        startDestination = BudgetsSettingsScreens.EditBudgets
    ) {
        composable<BudgetsSettingsScreens.EditBudgets> { backStack ->
            val editBudgetsViewModel = backStack.sharedKoinNavViewModel<EditBudgetsViewModel>(
                navController = navController
            )
            val editBudgetViewModel = backStack.sharedKoinNavViewModel<EditBudgetViewModel>(
                navController = navController
            )

            val budgetsByTypeState by editBudgetsViewModel.budgetsByType.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            EditBudgetsScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = isAppSetUp,
                budgetsByType = budgetsByTypeState,
                onNavigateToEditBudgetScreen = { budget: Budget? ->
                    editBudgetViewModel.applyBudget(budget)
                    navViewModel.navigateToScreen(navController, BudgetsSettingsScreens.EditBudget)
                },
                onSaveBudgetsButton = {
                    coroutineScope.launch {
                        editBudgetsViewModel.saveBudgets()
                        if (isAppSetUp) {
                            navController.popBackStack()
                        } else {
                            appViewModel.preFinishSetup()
                        }
                    }
                }
            )
        }
        composable<BudgetsSettingsScreens.EditBudget> { backStack ->
            val editBudgetsViewModel = backStack.sharedKoinNavViewModel<EditBudgetsViewModel>(
                navController = navController
            )
            val editBudgetViewModel = backStack.sharedKoinNavViewModel<EditBudgetViewModel>(
                navController = navController
            )

            val budget by editBudgetViewModel.budget.collectAsStateWithLifecycle()

            EditBudgetScreen(
                scaffoldPadding = scaffoldPadding,
                budget = budget,
                accountList = editBudgetViewModel.accounts,
                groupedCategoriesByType = editBudgetViewModel.groupedCategoriesByType,
                onNameChange = editBudgetViewModel::changeName,
                onCategoryChange = editBudgetViewModel::changeCategory,
                onAmountLimitChange = editBudgetViewModel::changeAmountLimit,
                onRepeatingPeriodChange = editBudgetViewModel::changeRepeatingPeriod,
                onLinkAccount = editBudgetViewModel::linkWithAccount,
                onUnlinkAccount = editBudgetViewModel::unlinkWithAccount,
                onDeleteButton = {
                    editBudgetsViewModel.deleteBudget(
                        id = budget.id, repeatingPeriod = budget.currRepeatingPeriod
                    )
                    navController.popBackStack()
                },
                onSaveButton = {
                    editBudgetsViewModel.applyBudget(editBudgetViewModel.getBudgetDraft())
                    navController.popBackStack()
                }
            )
        }
    }
}