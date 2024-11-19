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
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.billing.presentation.viewmodel.SubscriptionViewModel
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppUiState
import com.ataglance.walletglance.core.domain.componentState.SetupProgressTopBarUiState
import com.ataglance.walletglance.core.domain.widgets.WidgetsUiState
import com.ataglance.walletglance.core.presentation.components.pickers.DateRangeAssetsPickerContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.DimmedBackgroundOverlay
import com.ataglance.walletglance.core.presentation.components.screenContainers.MainScaffold
import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.utils.getGreetingsWidgetTitleRes
import com.ataglance.walletglance.navigation.domain.utils.anyScreenInHierarchyIs
import com.ataglance.walletglance.navigation.domain.utils.currentScreenIs
import com.ataglance.walletglance.navigation.domain.utils.getSetupProgressTopBarTitleRes
import com.ataglance.walletglance.navigation.presentation.AppNavHost
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel
import com.ataglance.walletglance.record.domain.utils.filterAccountId
import com.ataglance.walletglance.record.domain.utils.getExpensesIncomeWidgetUiState
import com.ataglance.walletglance.record.domain.utils.shrinkForCompactView
import com.ataglance.walletglance.settings.domain.ThemeUiState
import java.time.LocalDateTime

@Composable
fun MainAppContent(
    authController: AuthController,
    subscriptionViewModel: SubscriptionViewModel,
    appViewModel: AppViewModel,
    appConfiguration: AppConfiguration,
    themeUiState: ThemeUiState,
    navViewModel: NavigationViewModel,
    navController: NavHostController,
    personalizationViewModel: PersonalizationViewModel
) {
    var dimBackground by remember { mutableStateOf(false) }
    var openCustomDateRangeWindow by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val setupProgressTopBarUiState by remember(appConfiguration.isSetUp, navBackStackEntry) {
        derivedStateOf {
            SetupProgressTopBarUiState(
                isVisible = navViewModel.shouldDisplaySetupProgressTopBar(
                    appConfiguration.isSetUp, navBackStackEntry
                ),
                titleRes = navBackStackEntry.getSetupProgressTopBarTitleRes()
            )
        }
    }
    val isBottomBarVisible by remember(appConfiguration.isSetUp, navBackStackEntry) {
        derivedStateOf {
            navViewModel.shouldDisplayBottomNavigationBar(appConfiguration.isSetUp, navBackStackEntry)
        }
    }
    val moveScreenTowardsLeft by navViewModel.moveScreensTowardsLeft.collectAsStateWithLifecycle()
    val navigationButtonList by navViewModel.navigationButtonList.collectAsStateWithLifecycle()

    val widgetNamesList by personalizationViewModel.widgetNamesList.collectAsStateWithLifecycle()
    val budgetsOnWidget by personalizationViewModel.budgetsOnWidget.collectAsStateWithLifecycle()

    val currentLocalDateTime = LocalDateTime.now()
    val greetingsTitleRes by remember(currentLocalDateTime.hour) {
        derivedStateOf {
            currentLocalDateTime.hour.getGreetingsWidgetTitleRes()
        }
    }
    val dateRangeMenuUiState by appViewModel.dateRangeMenuUiState.collectAsStateWithLifecycle()
    val categoriesWithSubcategories by appViewModel.categoriesWithSubcategories
        .collectAsStateWithLifecycle()
    val categoryCollectionsUiState by appViewModel.categoryCollectionsUiState
        .collectAsStateWithLifecycle()
    val accountsUiState by appViewModel.accountsAndActiveOne.collectAsStateWithLifecycle()
    val recordStackListByDate by appViewModel.recordStackListFilteredByDate
        .collectAsStateWithLifecycle()
    val budgetsByType by appViewModel.budgetsByType.collectAsStateWithLifecycle()


    val appUiState by remember {
        derivedStateOf {
            AppUiState(
                navigationButtonList = navigationButtonList,
                dateRangeMenuUiState = dateRangeMenuUiState,
                categoriesWithSubcategories = categoriesWithSubcategories,
                categoryCollectionsUiState = categoryCollectionsUiState,
                accountsAndActiveOne = accountsUiState,
                recordStackListByDate = recordStackListByDate,
                budgetsByType = budgetsByType
            )
        }
    }
    val widgetsUiState by remember(
        widgetNamesList,
        budgetsOnWidget,
        greetingsTitleRes,
        categoriesWithSubcategories,
        accountsUiState,
        recordStackListByDate
    ) {
        derivedStateOf {
            val recordStacksByDateAndAccount = accountsUiState.activeAccount?.id
                ?.let { recordStackListByDate.filterAccountId(it) }
                ?: emptyList()

            WidgetsUiState(
                greetingsTitleRes = greetingsTitleRes,
                activeAccountExpensesForToday = appViewModel.getActiveAccountExpensesForToday(),
                recordStacksByDateAndAccount = recordStacksByDateAndAccount,

                widgetNamesList = widgetNamesList,
                budgetsOnWidget = budgetsByType
                    .concatenate()
                    .filter { it.id in budgetsOnWidget },
                expensesIncomeWidgetUiState = recordStacksByDateAndAccount
                    .getExpensesIncomeWidgetUiState(),
                compactRecordStacksByDateAndAccount = recordStacksByDateAndAccount
                    .shrinkForCompactView(),
                categoryStatisticsLists = categoriesWithSubcategories
                    .getStatistics(recordStacksByDateAndAccount)
            )
        }
    }

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
                        isNew = true, recordNum = appConfiguration.nextRecordNum()
                    )
                )
            },
            bottomBarButtons = navigationButtonList
        ) { scaffoldPadding ->
            AppNavHost(
                navController = navController,
                scaffoldPadding = scaffoldPadding,
                navViewModel = navViewModel,
                moveScreenTowardsLeft = moveScreenTowardsLeft,
                authController = authController,
                subscriptionViewModel = subscriptionViewModel,
                appViewModel = appViewModel,
                personalizationViewModel = personalizationViewModel,
                appConfiguration = appConfiguration,
                themeUiState = themeUiState,
                appUiState = appUiState,
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
        DimmedBackgroundOverlay(visible = dimBackground)
    }
}
