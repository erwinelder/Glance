package com.ataglance.walletglance.navigation.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ataglance.walletglance.budget.presentation.screen.BudgetStatisticsScreenWrapper
import com.ataglance.walletglance.budget.presentation.screen.BudgetsScreenWrapper
import com.ataglance.walletglance.category.presentation.screen.CategoryStatisticsScreenWrapper
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppUiState
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.animation.screenEnterTransition
import com.ataglance.walletglance.core.presentation.animation.screenExitTransition
import com.ataglance.walletglance.core.presentation.screen.HomeScreenWrapper
import com.ataglance.walletglance.core.presentation.screen.SetupFinishScreen
import com.ataglance.walletglance.core.presentation.screen.UpdateRequestScreen
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.record.presentation.screen.RecordCreationScreenWrapper
import com.ataglance.walletglance.settings.presentation.navigation.settingsGraph
import com.ataglance.walletglance.transaction.presentation.screen.TransactionsScreenWrapper
import com.ataglance.walletglance.transfer.presentation.screen.TransferCreationScreenWrapper
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    screenPadding: PaddingValues,
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appViewModel: AppViewModel,
    moveScreenTowardsLeft: Boolean,
    appConfiguration: AppConfiguration,
    appUiState: AppUiState,
    widgetNames: List<WidgetName>,
    openCustomDateRangeWindow: Boolean,
    onCustomDateRangeButtonClick: () -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = appConfiguration.mainStartDestination,
        contentAlignment = Alignment.Center,
        enterTransition = { screenEnterTransition(moveScreenTowardsLeft) },
        popEnterTransition = { screenEnterTransition(!moveScreenTowardsLeft) },
        exitTransition = { screenExitTransition(moveScreenTowardsLeft) },
        popExitTransition = { screenExitTransition(false) }
    ) {
        composable<MainScreens.Home>(
            popEnterTransition = { screenEnterTransition(false) }
        ) {
            HomeScreenWrapper(
                screenPadding = screenPadding,
                isAppThemeSetUp = appConfiguration.appTheme != null,
                accountsAndActiveOne = appUiState.accountsAndActiveOne,
                onTopBarAccountClick = appViewModel::applyActiveAccount,
                dateRangeWithEnum = appUiState.dateRangeWithEnum,
                onDateRangeChange = appViewModel::selectDateRange,
                isCustomDateRangeWindowOpened = openCustomDateRangeWindow,
                onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                widgetNames = widgetNames,
                onChangeHideActiveAccountBalance = appViewModel::onChangeHideActiveAccountBalance,
                onNavigateToScreenMovingTowardsLeft = { screen ->
                    navViewModel.navigateToScreenMovingTowardsLeft(navController, screen)
                }
            )
        }
        composable<MainScreens.Transactions> {
            TransactionsScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel,
                appViewModel = appViewModel,
                appUiState = appUiState,
                openCustomDateRangeWindow = openCustomDateRangeWindow,
                onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                onDimBackgroundChange = onDimBackgroundChange
            )
        }
        composable<MainScreens.CategoryStatistics> { backStack ->
            CategoryStatisticsScreenWrapper(
                screenPadding = screenPadding,
                backStack = backStack,
                navController = navController,
                navViewModel = navViewModel,
                appViewModel = appViewModel,
                appUiState = appUiState,
                openCustomDateRangeWindow = openCustomDateRangeWindow,
                onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                onDimBackgroundChange = onDimBackgroundChange
            )
        }
        composable<MainScreens.Budgets> {
            BudgetsScreenWrapper(
                screenPadding = screenPadding,
                navController = navController,
                navViewModel = navViewModel
            )
        }
        composable<MainScreens.BudgetStatistics> { backStack ->
            BudgetStatisticsScreenWrapper(
                screenPadding = screenPadding,
                backStack = backStack,
                navController = navController,
                appConfiguration = appConfiguration
            )
        }
        composable<MainScreens.RecordCreation>(
            enterTransition = { screenEnterTransition() },
            popEnterTransition = { screenEnterTransition(!moveScreenTowardsLeft) },
            exitTransition = { screenExitTransition(moveScreenTowardsLeft) },
            popExitTransition = { screenExitTransition(false) }
        ) { backStack ->
            RecordCreationScreenWrapper(
                screenPadding = screenPadding,
                backStack = backStack,
                navController = navController,
                onDimBackgroundChange = onDimBackgroundChange,
                appUiState = appUiState
            )
        }
        composable<MainScreens.TransferCreation>(
            enterTransition = { screenEnterTransition() },
            popExitTransition = { screenExitTransition(false) }
        ) { backStack ->
            TransferCreationScreenWrapper(
                screenPadding = screenPadding,
                backStack = backStack,
                navController = navController,
                navViewModel = navViewModel,
                appUiState = appUiState
            )
        }
        settingsGraph(
            screenPadding = screenPadding,
            navController = navController,
            navViewModel = navViewModel,
            appConfiguration = appConfiguration
        )
        composable<MainScreens.FinishSetup> {
            val coroutineScope = rememberCoroutineScope()

            SetupFinishScreen(
                onFinishSetupButton = {
                    coroutineScope.launch {
                        appViewModel.finishSetup()
                    }
                }
            )
        }
        composable<MainScreens.UpdateRequest> {
            UpdateRequestScreen(screenPadding = screenPadding)
        }
    }
}