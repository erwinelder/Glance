package com.ataglance.walletglance.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ataglance.walletglance.data.app.AppUiSettings
import com.ataglance.walletglance.data.makingRecord.MakeRecordStatus
import com.ataglance.walletglance.data.settings.ThemeUiState
import com.ataglance.walletglance.data.utils.fromMainScreen
import com.ataglance.walletglance.data.utils.needToMoveScreenTowardsLeft
import com.ataglance.walletglance.presentation.theme.DateRangeAssetsPickerContainer
import com.ataglance.walletglance.presentation.theme.DimmedBackgroundOverlay
import com.ataglance.walletglance.presentation.theme.navigation.screens.MainScreens
import com.ataglance.walletglance.presentation.theme.uielements.BottomNavBar
import com.ataglance.walletglance.presentation.theme.uielements.SetupProgressTopBar
import com.ataglance.walletglance.presentation.theme.uielements.shouldDisplaySetupProgressTopBar
import com.ataglance.walletglance.presentation.viewmodels.AppViewModel

@Composable
fun MainAppContent(
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    themeUiState: ThemeUiState,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var moveScreenTowardsLeft by remember { mutableStateOf(true) }

    val accountsUiState by appViewModel.accountsUiState.collectAsStateWithLifecycle()
    val categoriesWithSubcategories by appViewModel.categoriesWithSubcategories
        .collectAsStateWithLifecycle()
    val categoryCollectionsUiState by appViewModel.categoryCollectionsUiState
        .collectAsStateWithLifecycle()
    val dateRangeMenuUiState by appViewModel.dateRangeMenuUiState.collectAsStateWithLifecycle()
    val recordStackList by appViewModel.recordStackList.collectAsStateWithLifecycle()
    val budgetsByType by appViewModel.budgetsByType.collectAsStateWithLifecycle()
    val widgetsUiState by appViewModel.widgetsUiState.collectAsStateWithLifecycle()

    var dimBackground by remember { mutableStateOf(false) }
    var openCustomDateRangeWindow by remember { mutableStateOf(false) }

    Box {
        Scaffold(
            topBar = {
                SetupProgressTopBar(
                    visible = shouldDisplaySetupProgressTopBar(
                        appUiSettings.mainStartDestination, navBackStackEntry
                    ),
                    navBackStackEntry = navBackStackEntry,
                    onBackNavigationButton = navController::popBackStack
                )
            },
            bottomBar = {
                BottomNavBar(
                    appTheme = appUiSettings.appTheme,
                    isAppSetUp = appUiSettings.isSetUp,
                    navBackStackEntry = navBackStackEntry,
                    onNavigateBack = navController::popBackStack,
                    onNavigateToScreen = { screenNavigateTo: MainScreens ->
                        navBackStackEntry.fromMainScreen().let { currentScreen ->
                            moveScreenTowardsLeft = needToMoveScreenTowardsLeft(
                                currentScreen, screenNavigateTo
                            )
                        }
                        navController.navigate(screenNavigateTo) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    onMakeRecordButtonClick = {
                        moveScreenTowardsLeft = true
                        navController.navigate(
                            MainScreens.MakeRecord(
                                status = MakeRecordStatus.Create.name,
                                recordNum = appUiSettings.nextRecordNum()
                            )
                        ) {
                            launchSingleTop = true
                        }
                    }
                )
            },
            containerColor = Color.Transparent
        ) { scaffoldPadding ->
            AppNavHost(
                moveScreenTowardsLeft = moveScreenTowardsLeft,
                changeMoveScreenTowardsLeft = {
                    moveScreenTowardsLeft = it
                },
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
