package com.ataglance.walletglance.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.app.AppUiSettings
import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.budgets.BudgetsByType
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.presentation.ui.navigation.screens.BudgetsSettingsScreens
import com.ataglance.walletglance.presentation.ui.navigation.screens.SettingsScreens
import com.ataglance.walletglance.presentation.ui.screens.settings.budgets.EditBudgetScreen
import com.ataglance.walletglance.presentation.ui.screens.settings.budgets.EditBudgetsScreen
import com.ataglance.walletglance.presentation.viewmodels.AppViewModel
import com.ataglance.walletglance.presentation.viewmodels.budgets.EditBudgetViewModel
import com.ataglance.walletglance.presentation.viewmodels.budgets.EditBudgetsViewModel
import com.ataglance.walletglance.presentation.viewmodels.budgets.EditBudgetsViewModelFactory
import com.ataglance.walletglance.presentation.viewmodels.sharedViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.budgetsGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    budgetsByType: BudgetsByType,
    accountList: List<Account>,
    categoriesWithSubcategories: CategoriesWithSubcategories
) {
    navigation<SettingsScreens.Budgets>(
        startDestination = BudgetsSettingsScreens.EditBudgets
    ) {
        composable<BudgetsSettingsScreens.EditBudgets> { backStack ->
            val editBudgetsViewModel = backStack.sharedViewModel<EditBudgetsViewModel>(
                navController = navController,
                factory = EditBudgetsViewModelFactory(budgetsByType = budgetsByType)
            )
            val editBudgetViewModel = backStack.sharedViewModel<EditBudgetViewModel>(
                navController = navController
            )

            val budgetsByTypeState by editBudgetsViewModel.budgetsByType
                .collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            EditBudgetsScreen(
                scaffoldPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                budgetsByType = budgetsByTypeState,
                onNavigateToEditBudgetScreen = { budget: Budget? ->
                    editBudgetViewModel.applyBudget(
                        budget = budget?.toBudgetUiState(accountList),
                        categoryWithSubcategory = categoriesWithSubcategories.expense.getOrNull(0)
                            ?.getWithFirstSubcategory()
                    )
                    navController.navigate(BudgetsSettingsScreens.EditBudget)
                },
                onSaveBudgetsButton = {
                    coroutineScope.launch {
                        appViewModel.saveBudgetsToDb(
                            budgetList = editBudgetsViewModel.getBudgetList()
                        )
                    }
                    navController.popBackStack()
                }
            )
        }
        composable<BudgetsSettingsScreens.EditBudget> { backStack ->
            val editBudgetsViewModel = backStack.sharedViewModel<EditBudgetsViewModel>(
                navController = navController,
                factory = EditBudgetsViewModelFactory(budgetsByType = budgetsByType)
            )
            val editBudgetViewModel = backStack.sharedViewModel<EditBudgetViewModel>(
                navController = navController
            )

            val budgetState by editBudgetViewModel.budget.collectAsStateWithLifecycle()

            EditBudgetScreen(
                scaffoldPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                budget = budgetState,
                accountList = accountList,
                categoriesWithSubcategories = categoriesWithSubcategories,
                onNameChange = editBudgetViewModel::changeName,
                onCategoryChange = editBudgetViewModel::changeCategory,
                onAmountLimitChange = editBudgetViewModel::changeAmountLimit,
                onRepeatingPeriodChange = editBudgetViewModel::changeRepeatingPeriod,
                onLinkAccount = editBudgetViewModel::linkWithAccount,
                onUnlinkAccount = editBudgetViewModel::unlinkWithAccount,
                onDeleteButton = {
                    editBudgetsViewModel.deleteBudget(
                        id = budgetState.id,
                        repeatingPeriod = budgetState.currRepeatingPeriod
                    )
                    navController.popBackStack()
                },
                onSaveButton = {
                    editBudgetsViewModel.saveBudget(editBudgetViewModel.getBudgetUiState())
                    navController.popBackStack()
                }
            )
        }
    }
}