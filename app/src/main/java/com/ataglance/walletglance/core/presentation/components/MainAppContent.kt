package com.ataglance.walletglance.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ataglance.walletglance.core.domain.app.AppUiSettings
import com.ataglance.walletglance.core.domain.componentState.SetupProgressTopBarUiState
import com.ataglance.walletglance.core.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.components.containers.DimmedBackgroundOverlay
import com.ataglance.walletglance.core.presentation.components.containers.MainScaffold
import com.ataglance.walletglance.core.presentation.components.pickers.DateRangeAssetsPickerContainer
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.PersonalizationViewModel
import com.ataglance.walletglance.navigation.presentation.AppNavHost
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.navigation.utils.anyScreenInHierarchyIs
import com.ataglance.walletglance.navigation.utils.currentScreenIs
import com.ataglance.walletglance.navigation.utils.getSetupProgressTopBarTitleRes
import com.ataglance.walletglance.settings.domain.ThemeUiState

@Composable
fun MainAppContent(
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    themeUiState: ThemeUiState,
    navViewModel: NavigationViewModel,
    personalizationViewModel: PersonalizationViewModel
) {
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val moveScreenTowardsLeft by navViewModel.moveScreensTowardsLeft.collectAsStateWithLifecycle()
    val navigationButtonList by navViewModel.navigationButtonList.collectAsStateWithLifecycle()

    val setupProgressTopBarUiState by remember(appUiSettings.isSetUp, navBackStackEntry) {
        derivedStateOf {
            SetupProgressTopBarUiState(
                isVisible = navViewModel.shouldDisplaySetupProgressTopBar(
                    appUiSettings.isSetUp, navBackStackEntry
                ),
                titleRes = navBackStackEntry.getSetupProgressTopBarTitleRes()
            )
        }
    }
    val isBottomBarVisible by remember(appUiSettings.isSetUp, navBackStackEntry) {
        derivedStateOf {
            navViewModel.shouldDisplayBottomNavigationBar(appUiSettings.isSetUp, navBackStackEntry)
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

    val widgetNamesList by personalizationViewModel.widgetNamesList.collectAsStateWithLifecycle()

    Box {
        MainScaffold(
            setupProgressTopBarUiState = setupProgressTopBarUiState,
            isBottomBarVisible = isBottomBarVisible,
            anyScreenInHierarchyIsScreenProvider = navBackStackEntry::anyScreenInHierarchyIs,
            currentScreenIsScreenProvider = navBackStackEntry::currentScreenIs,
            onNavigateBack = navController::popBackStack,
            onNavigateToScreenAndPopUp = { screenNavigateTo: MainScreens ->
                navViewModel.navigateToScreenAndPopUp(
                    navController = navController,
                    navBackStackEntry = navBackStackEntry,
                    screenNavigateTo = screenNavigateTo
                )
            },
            onMakeRecordButtonClick = {
                navViewModel.navigateToScreenMovingTowardsLeft(
                    navController = navController,
                    screen = MainScreens.RecordCreation(
                        isNew = true, recordNum = appUiSettings.nextRecordNum()
                    )
                )
            },
            bottomBarButtons = navigationButtonList
        ) { scaffoldPadding ->
            AppNavHost(
                navController = navController,
                scaffoldPadding = scaffoldPadding,
                navViewModel = navViewModel,
                navigationButtonList = navigationButtonList,
                moveScreenTowardsLeft = moveScreenTowardsLeft,
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
                personalizationViewModel = personalizationViewModel,
                widgetNamesList = widgetNamesList,
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
        DimmedBackgroundOverlay(visible = dimBackground)
    }
}
