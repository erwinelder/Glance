package com.ataglance.walletglance.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ataglance.walletglance.category.presentation.model.CategoriesStatisticsByType
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppUiState
import com.ataglance.walletglance.core.domain.widgets.WidgetsUiState
import com.ataglance.walletglance.core.presentation.components.pickers.DateRangeAssetsPickerContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.DimmedBackgroundOverlay
import com.ataglance.walletglance.core.presentation.components.screenContainers.MainScaffold
import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.utils.getGreetingsWidgetTitleRes
import com.ataglance.walletglance.navigation.domain.utils.anyScreenInHierarchyIs
import com.ataglance.walletglance.navigation.domain.utils.currentScreenIs
import com.ataglance.walletglance.navigation.presentation.AppNavHost
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.record.domain.utils.filterByAccount
import com.ataglance.walletglance.record.domain.utils.shrinkForCompactView
import com.ataglance.walletglance.record.mapper.getExpensesIncomeWidgetUiState
import java.time.LocalDateTime

@Composable
fun MainAppContent(
    appConfiguration: AppConfiguration,
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appViewModel: AppViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isBottomBarVisible by remember(appConfiguration.isSetUp, navBackStackEntry) {
        derivedStateOf {
            navViewModel.shouldDisplayBottomNavigationBar(appConfiguration.isSetUp, navBackStackEntry)
        }
    }
    val moveScreenTowardsLeft by navViewModel.moveScreensTowardsLeft.collectAsStateWithLifecycle()
    val navigationButtons by navViewModel.navigationButtonList.collectAsStateWithLifecycle()

    val accountsUiState by appViewModel.accountsAndActiveOne.collectAsState()
    val dateRangeMenuUiState by appViewModel.dateRangeMenuUiState.collectAsStateWithLifecycle()
    val recordStacksInDateRange by appViewModel.recordStacksInDateRange.collectAsStateWithLifecycle()

    val widgetNames by appViewModel.widgetNames.collectAsStateWithLifecycle()

    var dimBackground by remember { mutableStateOf(false) }
    var openCustomDateRangeWindow by remember { mutableStateOf(false) }

    val currentLocalDateTime = LocalDateTime.now()
    val greetingsTitleRes by remember(currentLocalDateTime.hour) {
        derivedStateOf {
            currentLocalDateTime.hour.getGreetingsWidgetTitleRes()
        }
    }


    val appUiState by remember {
        derivedStateOf {
            AppUiState(
                dateRangeMenuUiState = dateRangeMenuUiState,
                accountsAndActiveOne = accountsUiState
            )
        }
    }
    val widgetsUiState by remember(
        widgetNames,
        greetingsTitleRes,
        accountsUiState,
        recordStacksInDateRange
    ) {
        derivedStateOf {
            val recordStacksByDateAndAccount = accountsUiState.activeAccount?.id
                ?.let { recordStacksInDateRange.recordStacks.filterByAccount(it) }
                .orEmpty()

            WidgetsUiState(
                greetingsTitleRes = greetingsTitleRes,
                activeAccountExpensesForToday = appViewModel.getActiveAccountExpensesForToday(),

                widgetNamesList = widgetNames,
                expensesIncomeWidgetUiState = recordStacksByDateAndAccount.getExpensesIncomeWidgetUiState(),
                compactRecordStacksByDateAndAccount = recordStacksByDateAndAccount.shrinkForCompactView(),
                categoriesStatisticsByType = CategoriesStatisticsByType.fromRecordStacks(recordStacksByDateAndAccount)
            )
        }
    }

    Box {
        MainScaffold(
            isBottomBarVisible = isBottomBarVisible,
            anyScreenInHierarchyIsScreenProvider = navBackStackEntry::anyScreenInHierarchyIs,
            currentScreenIsScreenProvider = navBackStackEntry::currentScreenIs,
            onNavigateToScreenAndPopUp = { screenNavigateTo: MainScreens ->
                navViewModel.navigateToScreenPoppingToStartDestination(
                    navController = navController,
                    navBackStackEntry = navBackStackEntry,
                    screenNavigateTo = screenNavigateTo
                )
            },
            onMakeRecordButtonClick = {
                navViewModel.navigateToScreenMovingTowardsLeft(
                    navController = navController,
                    screen = MainScreens.RecordCreation()
                )
            },
            bottomBarButtons = navigationButtons
        ) { scaffoldPadding ->
            AppNavHost(
                navController = navController,
                scaffoldPadding = scaffoldPadding,
                navViewModel = navViewModel,
                appViewModel = appViewModel,
                moveScreenTowardsLeft = moveScreenTowardsLeft,
                appConfiguration = appConfiguration,
                appUiState = appUiState,
                widgetsUiState = widgetsUiState,
                openCustomDateRangeWindow = openCustomDateRangeWindow,
                onCustomDateRangeButtonClick = { openCustomDateRangeWindow = !openCustomDateRangeWindow },
                onDimBackgroundChange = { dimBackground = it }
            )
            DateRangeAssetsPickerContainer(
                scaffoldPadding = scaffoldPadding,
                dateRangeMenuUiState = dateRangeMenuUiState,
                openCustomDateRangeWindow = openCustomDateRangeWindow,
                onCloseCustomDateRangeWindow = { openCustomDateRangeWindow = false },
                onDateRangeSelect = appViewModel::selectDateRange,
                onCustomDateRangeSelect = appViewModel::selectCustomDateRange
            )
        }
        DimmedBackgroundOverlay(visible = dimBackground)
    }
}
