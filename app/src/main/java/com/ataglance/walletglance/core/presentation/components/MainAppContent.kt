package com.ataglance.walletglance.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ataglance.walletglance.core.domain.app.AppUiSettings
import com.ataglance.walletglance.core.navigation.AppNavHost
import com.ataglance.walletglance.core.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.components.containers.BottomNavBar
import com.ataglance.walletglance.core.presentation.components.containers.DimmedBackgroundOverlay
import com.ataglance.walletglance.core.presentation.components.pickers.DateRangeAssetsPickerContainer
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.ThemeUiState
import com.ataglance.walletglance.settings.presentation.components.SetupProgressTopBar

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
                    visible = navViewModel.shouldDisplaySetupProgressTopBar(
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
                        navViewModel.navigateToScreen(
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
                    }
                )
            },
            containerColor = Color.Transparent
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



@Preview
@Composable
fun MainAppContentHomeScreenPreview() {

}
