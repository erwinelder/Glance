package com.ataglance.walletglance.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ataglance.walletglance.core.domain.app.AppUiSettings
import com.ataglance.walletglance.core.domain.componentState.SetupProgressTopBarUiState
import com.ataglance.walletglance.core.navigation.AppNavHost
import com.ataglance.walletglance.core.navigation.BottomBarNavigationButtons
import com.ataglance.walletglance.core.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.components.containers.DimmedBackgroundOverlay
import com.ataglance.walletglance.core.presentation.components.containers.MainScaffold
import com.ataglance.walletglance.core.presentation.components.pickers.DateRangeAssetsPickerContainer
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.core.utils.anyScreenInHierarchyIs
import com.ataglance.walletglance.core.utils.currentScreenIs
import com.ataglance.walletglance.core.utils.currentScreenIsOneOf
import com.ataglance.walletglance.core.utils.getSetupProgressTopBarTitleRes
import com.ataglance.walletglance.settings.domain.ThemeUiState
import com.ataglance.walletglance.settings.navigation.SettingsScreens

@Composable
fun MainAppContent(
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    themeUiState: ThemeUiState,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navViewModel = viewModel<NavigationViewModel>()
    val moveScreenTowardsLeft by navViewModel.moveScreenTowardsLeft.collectAsStateWithLifecycle()

    val setupProgressTopBarUiState by remember {
        derivedStateOf {
            SetupProgressTopBarUiState(
                isVisible = navViewModel.shouldDisplaySetupProgressTopBar(
                    appUiSettings.mainStartDestination, navBackStackEntry
                ),
                titleRes = navBackStackEntry.getSetupProgressTopBarTitleRes()
            )
        }
    }
    val isBottomBarVisible by remember {
        derivedStateOf {
            appUiSettings.isSetUp && navBackStackEntry.currentScreenIsOneOf(
                MainScreens.Home, MainScreens.Records, MainScreens.CategoryStatistics(0),
                MainScreens.Budgets, SettingsScreens.SettingsHome, SettingsScreens.Language
            )
        }
    }

    var dimBackground by remember { mutableStateOf(false) }
    var openCustomDateRangeWindow by remember { mutableStateOf(false) }

    val accountsUiState by appViewModel.accountsUiState.collectAsStateWithLifecycle()
    val categoriesWithSubcategories by appViewModel.categoriesWithSubcategories
        .collectAsStateWithLifecycle()
    val categoryCollectionsUiState by appViewModel.categoryCollectionsUiState
        .collectAsStateWithLifecycle()
    val dateRangeMenuUiState by appViewModel.dateRangeMenuUiState.collectAsStateWithLifecycle()
    val recordStackList by appViewModel.recordStackList.collectAsStateWithLifecycle()
    val budgetsByType by appViewModel.budgetsByType.collectAsStateWithLifecycle()
    val widgetsUiState by appViewModel.widgetsUiState.collectAsStateWithLifecycle()

    Box {
        MainScaffold(
            appTheme = appUiSettings.appTheme,
            setupProgressTopBarUiState = setupProgressTopBarUiState,
            isBottomBarVisible = isBottomBarVisible,
            anyScreenInHierarchyIsScreenProvider = navBackStackEntry::anyScreenInHierarchyIs,
            currentScreenIsScreenProvider = navBackStackEntry::currentScreenIs,
            onNavigateBack = navController::popBackStack,
            onNavigateToScreen = { screenNavigateTo: MainScreens ->
                navViewModel.navigateToScreenAndPopUp(
                    navController = navController,
                    navBackStackEntry = navBackStackEntry,
                    screenNavigateTo = screenNavigateTo
                )
            },
            onMakeRecordButtonClick = {
                navViewModel.onMakeRecordButtonClick(
                    navController = navController,
                    recordNum = appUiSettings.nextRecordNum()
                )
            },
            bottomBarButtons = listOf(
                BottomBarNavigationButtons.Home,
                BottomBarNavigationButtons.Records,
                BottomBarNavigationButtons.CategoryStatistics,
                BottomBarNavigationButtons.Budgets,
                BottomBarNavigationButtons.Settings
            )
        ) { scaffoldPadding ->
            AppNavHost(
                moveScreenTowardsLeft = moveScreenTowardsLeft,
                changeMoveScreenTowardsLeft = navViewModel::setMoveScreenTowardsLeft,
                navController = navController,
                scaffoldPadding = scaffoldPadding,
                appViewModel = appViewModel,
                appUiSettings = appUiSettings,
                themeUiState = themeUiState,
                accountsUiState = accountsUiState,
                categoriesWithSubcategories = categoriesWithSubcategories,
                categoryCollectionsUiState = categoryCollectionsUiState,
                dateRangeMenuUiState = dateRangeMenuUiState,
                recordStackList = recordStackList,
                budgetsByType = budgetsByType,
                widgetsUiState = widgetsUiState,
                openCustomDateRangeWindow = openCustomDateRangeWindow,
                onCustomDateRangeButtonClick = {
                    openCustomDateRangeWindow = !openCustomDateRangeWindow
                },
                onDimBackgroundChange = { value: Boolean ->
                    dimBackground = value
                }
            )
            DateRangeAssetsPickerContainer(
                scaffoldPadding = scaffoldPadding,
                dateRangeMenuUiState = dateRangeMenuUiState,
                openCustomDateRangeWindow = openCustomDateRangeWindow,
                onCloseCustomDateRangeWindow = {
                    openCustomDateRangeWindow = false
                },
                onDateRangeSelect = appViewModel::selectDateRange,
                onCustomDateRangeSelect = appViewModel::selectCustomDateRange
            )
        }
        DimmedBackgroundOverlay(visible = dimBackground, appTheme = appUiSettings.appTheme)
    }
}
