package com.ataglance.walletglance.budget.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetScreen
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetsScreen
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetsViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.EditBudgetsViewModelFactory
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.navigation.SettingsScreens
import kotlinx.coroutines.launch

fun NavGraphBuilder.budgetsGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    navViewModel: NavigationViewModel,
    appViewModel: AppViewModel,
    isAppSetUp: Boolean,
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
                isAppSetUp = isAppSetUp,
                budgetsByType = budgetsByTypeState,
                onNavigateToEditBudgetScreen = { budget: Budget? ->
                    editBudgetViewModel.applyBudget(
                        budget = budget?.toBudgetUiState(accountList),
                        categoryWithSubcategory = categoriesWithSubcategories.expense.getOrNull(0)
                            ?.getWithFirstSubcategory()
                    )
                    navViewModel.navigateToScreen(navController, BudgetsSettingsScreens.EditBudget)
                },
                onSaveBudgetsButton = {
                    coroutineScope.launch {
                        appViewModel.saveBudgetsToDb(
                            budgetList = editBudgetsViewModel.getBudgetList()
                        )
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
            val editBudgetsViewModel = backStack.sharedViewModel<EditBudgetsViewModel>(
                navController = navController,
                factory = EditBudgetsViewModelFactory(budgetsByType = budgetsByType)
            )
            val editBudgetViewModel = backStack.sharedViewModel<EditBudgetViewModel>(
                navController = navController
            )

            val budgetUiState by editBudgetViewModel.budget.collectAsStateWithLifecycle()

            EditBudgetScreen(
                scaffoldPadding = scaffoldPadding,
                budget = budgetUiState,
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
                        id = budgetUiState.id,
                        repeatingPeriod = budgetUiState.currRepeatingPeriod
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