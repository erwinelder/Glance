package com.ataglance.walletglance.ui.theme.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.CategoryWithSubcategories
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.categories.icons.CategoryPossibleIcons
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionsWithIds
import com.ataglance.walletglance.data.records.MakeRecordStatus
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.ui.theme.animation.screenEnterTransition
import com.ataglance.walletglance.ui.theme.animation.screenExitTransition
import com.ataglance.walletglance.ui.theme.navigation.screens.AccountsSettingsScreens
import com.ataglance.walletglance.ui.theme.navigation.screens.CategoriesSettingsScreens
import com.ataglance.walletglance.ui.theme.navigation.screens.CategoryCollectionsSettingsScreens
import com.ataglance.walletglance.ui.theme.navigation.screens.MainScreens
import com.ataglance.walletglance.ui.theme.navigation.screens.SettingsScreens
import com.ataglance.walletglance.ui.theme.screens.settings.SettingsDataScreen
import com.ataglance.walletglance.ui.theme.screens.settings.SettingsHomeScreen
import com.ataglance.walletglance.ui.theme.screens.settings.SetupAppearanceScreen
import com.ataglance.walletglance.ui.theme.screens.settings.SetupFinishScreen
import com.ataglance.walletglance.ui.theme.screens.settings.SetupLanguageScreen
import com.ataglance.walletglance.ui.theme.screens.settings.SetupStartScreen
import com.ataglance.walletglance.ui.theme.screens.settings.accounts.CurrencyPickerScreen
import com.ataglance.walletglance.ui.theme.screens.settings.accounts.EditAccountScreen
import com.ataglance.walletglance.ui.theme.screens.settings.accounts.SetupAccountsScreen
import com.ataglance.walletglance.ui.theme.screens.settings.categories.EditCategoryScreen
import com.ataglance.walletglance.ui.theme.screens.settings.categories.EditSubcategoryListScreen
import com.ataglance.walletglance.ui.theme.screens.settings.categories.SetupCategoriesScreen
import com.ataglance.walletglance.ui.theme.screens.settings.categoryCollections.EditCategoryCollectionScreen
import com.ataglance.walletglance.ui.theme.screens.settings.categoryCollections.SetupCategoryCollectionsScreen
import com.ataglance.walletglance.ui.theme.uielements.BottomNavBar
import com.ataglance.walletglance.ui.theme.uielements.SetupProgressTopBar
import com.ataglance.walletglance.ui.theme.uielements.containers.CustomDateRangeWindow
import com.ataglance.walletglance.ui.theme.uielements.pickers.CustomDateRangePicker
import com.ataglance.walletglance.ui.utils.currentScreenIs
import com.ataglance.walletglance.ui.utils.fromMainScreen
import com.ataglance.walletglance.ui.utils.getMakeRecordStateAndUnitList
import com.ataglance.walletglance.ui.utils.getMakeTransferState
import com.ataglance.walletglance.ui.utils.needToMoveScreenTowardsLeft
import com.ataglance.walletglance.ui.utils.toAccountEntityList
import com.ataglance.walletglance.ui.utils.toCategoryEntityList
import com.ataglance.walletglance.ui.utils.toCollectionsWithIds
import com.ataglance.walletglance.ui.viewmodels.AccountsUiState
import com.ataglance.walletglance.ui.viewmodels.AppUiSettings
import com.ataglance.walletglance.ui.viewmodels.AppViewModel
import com.ataglance.walletglance.ui.viewmodels.DateRangeMenuUiState
import com.ataglance.walletglance.ui.viewmodels.ThemeUiState
import com.ataglance.walletglance.ui.viewmodels.WidgetsUiState
import com.ataglance.walletglance.ui.viewmodels.accounts.CurrencyPickerViewModel
import com.ataglance.walletglance.ui.viewmodels.accounts.CurrencyPickerViewModelFactory
import com.ataglance.walletglance.ui.viewmodels.accounts.EditAccountUiState
import com.ataglance.walletglance.ui.viewmodels.accounts.EditAccountViewModel
import com.ataglance.walletglance.ui.viewmodels.accounts.EditAccountViewModelFactory
import com.ataglance.walletglance.ui.viewmodels.accounts.EditAccountsViewModel
import com.ataglance.walletglance.ui.viewmodels.accounts.EditAccountsViewModelFactory
import com.ataglance.walletglance.ui.viewmodels.categories.CategoryStatisticsViewModel
import com.ataglance.walletglance.ui.viewmodels.categories.CategoryStatisticsViewModelFactory
import com.ataglance.walletglance.ui.viewmodels.categories.SetupCategoriesViewModel
import com.ataglance.walletglance.ui.viewmodels.categories.SetupCategoriesViewModelFactory
import com.ataglance.walletglance.ui.viewmodels.categoryCollections.CategoryCollectionsViewModel
import com.ataglance.walletglance.ui.viewmodels.categoryCollections.CategoryCollectionsViewModelFactory
import com.ataglance.walletglance.ui.viewmodels.categoryCollections.EditCategoryCollectionViewModel
import com.ataglance.walletglance.ui.viewmodels.categoryCollections.EditCategoryCollectionViewModelFactory
import com.ataglance.walletglance.ui.viewmodels.records.MakeRecordUiState
import com.ataglance.walletglance.ui.viewmodels.records.MakeRecordViewModel
import com.ataglance.walletglance.ui.viewmodels.records.MakeRecordViewModelFactory
import com.ataglance.walletglance.ui.viewmodels.records.MakeTransferViewModel
import com.ataglance.walletglance.ui.viewmodels.records.MakeTransferViewModelFactory
import com.ataglance.walletglance.ui.viewmodels.settings.LanguageViewModel
import com.ataglance.walletglance.ui.viewmodels.sharedViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    themeUiState: ThemeUiState,
    navController: NavHostController = rememberNavController()
) {
    var moveScreenTowardsLeft by remember { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val accountsUiState by appViewModel.accountsUiState.collectAsStateWithLifecycle()
    val categoriesWithSubcategories by appViewModel.categoriesWithSubcategories
        .collectAsStateWithLifecycle()
    val categoryCollectionsUiState by appViewModel.categoryCollectionsUiState
        .collectAsStateWithLifecycle()
    val dateRangeMenuUiState by appViewModel.dateRangeMenuUiState.collectAsStateWithLifecycle()
    val recordStackList by appViewModel.recordStackList.collectAsStateWithLifecycle()
    val widgetsUiState by appViewModel.widgetsUiState.collectAsStateWithLifecycle()

    var openCustomDateRangeWindow by remember { mutableStateOf(false) }
    var openDateRangePickerDialog by remember { mutableStateOf(false) }
    val dateRangePickerState = rememberDateRangePickerState()
    dateRangePickerState.setSelection(
        dateRangeMenuUiState.startCalendarDateMillis,
        dateRangeMenuUiState.endCalendarDateMillis
    )

    Scaffold(
        topBar = {
            SetupProgressTopBar(
                visible = appUiSettings.startMainDestination != MainScreens.Home &&
                        !navBackStackEntry.currentScreenIs(SettingsScreens.Start) &&
                        !navBackStackEntry.currentScreenIs(MainScreens.FinishSetup),
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
        HomeNavHost(
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
            widgetsUiState = widgetsUiState,
            openCustomDateRangeWindow = openCustomDateRangeWindow,
            onCustomDateRangeButtonClick = {
                openCustomDateRangeWindow = !openCustomDateRangeWindow
            }
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomDateRangeWindow(
                visible = openCustomDateRangeWindow,
                padding = PaddingValues(
                    top = scaffoldPadding.calculateTopPadding() +
                            scaffoldPadding.calculateTopPadding(),
                    bottom = scaffoldPadding.calculateBottomPadding() +
                            scaffoldPadding.calculateBottomPadding(),
                    end = 16.dp
                ),
                currentDateRangeEnum = dateRangeMenuUiState.dateRangeState.enum,
                dateRangePickerState = dateRangePickerState,
                onDismissRequest = {
                    openCustomDateRangeWindow = false
                },
                onDateRangeEnumClick = { dateRangeEnum ->
                    appViewModel.changeDateRange(dateRangeEnum)
                    openCustomDateRangeWindow = false
                },
                onCustomDateRangeFieldClick = {
                    openDateRangePickerDialog = true
                },
                onConfirmButtonClick = {
                    openCustomDateRangeWindow = false
                    appViewModel.changeDateRangeToCustom(
                        dateRangePickerState.selectedStartDateMillis,
                        dateRangePickerState.selectedEndDateMillis
                    )
                }
            )
            CustomDateRangePicker(
                state = dateRangePickerState,
                openDialog = openDateRangePickerDialog,
                onOpenDialogChange = {
                    openDateRangePickerDialog = it
                }
            )
        }
    }
}

@Composable
fun HomeNavHost(
    moveScreenTowardsLeft: Boolean,
    changeMoveScreenTowardsLeft: (Boolean) -> Unit,
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    themeUiState: ThemeUiState,
    accountsUiState: AccountsUiState,
    categoriesWithSubcategories: CategoriesWithSubcategories,
    categoryCollectionsUiState: CategoryCollectionsWithIds,
    dateRangeMenuUiState: DateRangeMenuUiState,
    recordStackList: List<RecordStack>,
    widgetsUiState: WidgetsUiState,
    openCustomDateRangeWindow: Boolean,
    onCustomDateRangeButtonClick: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = appUiSettings.startMainDestination,
        contentAlignment = Alignment.Center,
        enterTransition = { screenEnterTransition(moveScreenTowardsLeft) },
        popEnterTransition = { screenEnterTransition(!moveScreenTowardsLeft) },
        exitTransition = { screenExitTransition(moveScreenTowardsLeft) },
        popExitTransition = { screenExitTransition(false) }
    ) {
        composable<MainScreens.Home>(
            popEnterTransition = { screenEnterTransition(false) }
        ) {
            HomeScreen(
                scaffoldAppScreenPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                accountsUiState = accountsUiState,
                dateRangeMenuUiState = dateRangeMenuUiState,
                widgetsUiState = widgetsUiState,
                onChangeHideActiveAccountBalance = appViewModel::onChangeHideActiveAccountBalance,
                onDateRangeChange = appViewModel::changeDateRange,
                onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                onTopBarAccountClick = { orderNum ->
                    appViewModel.applyActiveAccountByOrderNum(orderNum)
                },
                isCustomDateRangeWindowOpened = openCustomDateRangeWindow,
                onNavigateToRecordsScreen = {
                    changeMoveScreenTowardsLeft(true)
                    navController.navigate(MainScreens.Records)
                },
                onNavigateToCategoriesStatisticsScreen = { parentCategoryId: Int ->
                    changeMoveScreenTowardsLeft(true)
                    navController.navigate(MainScreens.CategoryStatistics(parentCategoryId)) {
                        launchSingleTop = true
                    }
                },
                onRecordClick = { recordNum: Int ->
                    changeMoveScreenTowardsLeft(true)
                    navController.navigate(
                        MainScreens.MakeRecord(MakeRecordStatus.Edit.name, recordNum)
                    ) {
                        launchSingleTop = true
                    }
                },
                onTransferClick = { recordNum: Int ->
                    changeMoveScreenTowardsLeft(true)
                    navController.navigate(
                        MainScreens.MakeTransfer(MakeRecordStatus.Edit.name, recordNum)
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<MainScreens.Records> {
            RecordsScreen(
                scaffoldAppScreenPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                accountList = accountsUiState.accountList,
                recordStackList = widgetsUiState.filteredRecordStackList,
                onAccountClick = { orderNum ->
                    appViewModel.applyActiveAccountByOrderNum(orderNum)
                },
                currentDateRangeEnum = dateRangeMenuUiState.dateRangeState.enum,
                isCustomDateRangeWindowOpened = openCustomDateRangeWindow,
                onDateRangeChange = appViewModel::changeDateRange,
                onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                onRecordClick = { recordNum: Int ->
                    changeMoveScreenTowardsLeft(true)
                    navController.navigate(
                        MainScreens.MakeRecord(MakeRecordStatus.Edit.name, recordNum)
                    ) {
                        launchSingleTop = true
                    }
                },
                onTransferClick = { recordNum: Int ->
                    changeMoveScreenTowardsLeft(true)
                    navController.navigate(
                        MainScreens.MakeTransfer(MakeRecordStatus.Edit.name, recordNum)
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<MainScreens.CategoryStatistics> { backStack ->
            val parentCategoryId =
                backStack.toRoute<MainScreens.CategoryStatistics>().parentCategoryId

            val viewModel = viewModel<CategoryStatisticsViewModel>(
                factory = CategoryStatisticsViewModelFactory(
                    categoryStatisticsLists = widgetsUiState.categoryStatisticsLists
                )
            )
            LaunchedEffect(widgetsUiState.categoryStatisticsLists) {
                viewModel.setCategoryStatisticsLists(widgetsUiState.categoryStatisticsLists)
            }

            CategoriesStatisticsScreen(
                scaffoldAppScreenPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                accountList = accountsUiState.accountList,
                onAccountClick = { orderNum ->
                    appViewModel.applyActiveAccountByOrderNum(orderNum)
                },
                currentDateRangeEnum = dateRangeMenuUiState.dateRangeState.enum,
                isCustomDateRangeWindowOpened = openCustomDateRangeWindow,
                onDateRangeChange = appViewModel::changeDateRange,
                onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                viewModel = viewModel,
                parentCategoryId = parentCategoryId
            )
        }
        composable<MainScreens.MakeRecord>(
            enterTransition = { screenEnterTransition() },
            popEnterTransition = { screenEnterTransition(!moveScreenTowardsLeft) },
            exitTransition = { screenExitTransition(moveScreenTowardsLeft) },
            popExitTransition = { screenExitTransition(false) }
        ) { backStack ->
            val makeRecordStatus = MakeRecordStatus.valueOf(
                backStack.toRoute<MainScreens.MakeRecord>().status
            )
            val recordNum = backStack.toRoute<MainScreens.MakeRecord>().recordNum

            val makeRecordUiStateAndUnitList = recordStackList.getMakeRecordStateAndUnitList(
                makeRecordStatus = makeRecordStatus,
                recordNum = recordNum,
                accountList = accountsUiState.accountList
            ) ?: (MakeRecordUiState(
                recordStatus = MakeRecordStatus.Create,
                recordNum = appUiSettings.nextRecordNum(),
                account = accountsUiState.activeAccount
            ) to null)
            val categoryWithSubcategory = if (
                makeRecordUiStateAndUnitList.second == null && accountsUiState.activeAccount != null
            ) {
                appViewModel.getLastRecordCategory(accountId = accountsUiState.activeAccount.id)
            } else null
            val viewModel = viewModel<MakeRecordViewModel>(
                factory = MakeRecordViewModelFactory(
                    categoryWithSubcategory = categoryWithSubcategory,
                    makeRecordUiState = makeRecordUiStateAndUnitList.first,
                    makeRecordUnitList = makeRecordUiStateAndUnitList.second
                )
            )
            val coroutineScope = rememberCoroutineScope()

            MakeRecordScreen(
                appTheme = appUiSettings.appTheme,
                viewModel = viewModel,
                makeRecordStatus = makeRecordStatus,
                accountList = accountsUiState.accountList,
                categoriesWithSubcategories = categoriesWithSubcategories,
                onMakeTransferButtonClick = {
                    navController.navigate(
                        MainScreens.MakeTransfer(
                            status = MakeRecordStatus.Create.name,
                            recordNum = appUiSettings.nextRecordNum()
                        )
                    ) {
                        launchSingleTop = true
                    }
                },
                onSaveButton = { state, unitList ->
                    coroutineScope.launch {
                        appViewModel.saveRecord(state, unitList)
                    }
                    navController.popBackStack()
                },
                onRepeatButton = { state, unitList ->
                    coroutineScope.launch {
                        appViewModel.repeatRecord(state, unitList)
                    }
                    navController.popBackStack()
                },
                onDeleteButton = { recordNumToDelete ->
                    coroutineScope.launch {
                        appViewModel.deleteRecord(recordNumToDelete)
                    }
                    navController.popBackStack()
                },
            )
        }
        composable<MainScreens.MakeTransfer>(
            enterTransition = { screenEnterTransition() },
            popExitTransition = { screenExitTransition(false) }
        ) { backStack ->
            val makeRecordStatus = MakeRecordStatus.valueOf(
                backStack.toRoute<MainScreens.MakeTransfer>().status
            )
            val recordNum = backStack.toRoute<MainScreens.MakeTransfer>().recordNum

            val viewModel = viewModel<MakeTransferViewModel>(
                factory = MakeTransferViewModelFactory(
                    accountList = accountsUiState.accountList,
                    makeTransferUiState = recordStackList
                        .getMakeTransferState(makeRecordStatus, recordNum, accountsUiState)
                )
            )
            val coroutineScope = rememberCoroutineScope()

            MakeTransferScreen(
                appTheme = appUiSettings.appTheme,
                viewModel = viewModel,
                makeRecordStatus = makeRecordStatus,
                accountList = accountsUiState.accountList,
                onNavigateBack = navController::popBackStack,
                onSaveButton = { state ->
                    coroutineScope.launch {
                        appViewModel.saveTransfer(state)
                    }
                    navController.popBackStack(MainScreens.Home, false)
                },
                onRepeatButton = { state ->
                    coroutineScope.launch {
                        appViewModel.repeatTransfer(state)
                    }
                    navController.popBackStack(MainScreens.Home, false)
                },
                onDeleteButton = { recordNumToDelete ->
                    coroutineScope.launch {
                        appViewModel.deleteTransfer(recordNumToDelete)
                    }
                    navController.popBackStack(MainScreens.Home, false)
                }
            )
        }
        settingsGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            appViewModel = appViewModel,
            appUiSettings = appUiSettings,
            themeUiState = themeUiState,
            accountList = accountsUiState.accountList,
            categoriesWithSubcategories = categoriesWithSubcategories,
            categoryCollectionsUiState = categoryCollectionsUiState
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
    }
}


fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    themeUiState: ThemeUiState,
    accountList: List<Account>,
    categoriesWithSubcategories: CategoriesWithSubcategories,
    categoryCollectionsUiState: CategoryCollectionsWithIds
) {
    navigation<MainScreens.Settings>(startDestination = appUiSettings.startSettingsDestination) {
        composable<SettingsScreens.Start> {
            SetupStartScreen(
                appTheme = appUiSettings.appTheme,
                onManualSetupButton = {
                    navController.navigate(SettingsScreens.Language)
                }
            )
        }
        composable<SettingsScreens.SettingsHome> {
            SettingsHomeScreen(
                scaffoldPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                onNavigateToScreen = { screen: SettingsScreens ->
                    navController.navigate(screen) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<SettingsScreens.Language> {
            val viewModel = viewModel<LanguageViewModel>()
            val chosenLanguage by viewModel.langCode.collectAsState()
            if (chosenLanguage == null) {
                viewModel.chooseNewLanguage(appUiSettings.langCode)
            }

            SetupLanguageScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = appUiSettings.isSetUp,
                appLanguage = appUiSettings.langCode,
                chosenLanguage = chosenLanguage,
                chooseNewLanguage = viewModel::chooseNewLanguage,
                onApplyButton = { langCode: String ->
                    appViewModel.setLanguage(langCode)
                },
                onContextChange = appViewModel::translateDefaultCategories,
                onNextNavigationButton = {
                    navController.navigate(SettingsScreens.Appearance)
                }
            )
        }
        composable<SettingsScreens.Appearance> {
            SetupAppearanceScreen(
                isAppSetUp = appUiSettings.isSetUp,
                themeUiState = themeUiState,
                onContinueSetupButton = {
                    navController.navigate(SettingsScreens.Accounts)
                },
                onChooseLightTheme = appViewModel::chooseLightTheme,
                onChooseDarkTheme = appViewModel::chooseDarkTheme,
                onSetUseDeviceTheme = appViewModel::setUseDeviceTheme
            )
        }
        accountsGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            appViewModel = appViewModel,
            appUiSettings = appUiSettings,
            accountList = accountList
        )
        categoriesGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            appViewModel = appViewModel,
            appUiSettings = appUiSettings,
            categoriesWithSubcategories = categoriesWithSubcategories
        )
        categoryCollectionsGraph(
            navController = navController,
            appViewModel = appViewModel,
            categoriesWithSubcategories = categoriesWithSubcategories,
            categoryCollectionsWithIds = categoryCollectionsUiState
        )
        composable<SettingsScreens.ResetData> {
            val coroutineScope = rememberCoroutineScope()

            SettingsDataScreen(
                onResetData = {
                    coroutineScope.launch {
                        appViewModel.resetAppData()
                    }
                },
                onExportData = {}
            )
        }
    }
}

fun NavGraphBuilder.accountsGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    accountList: List<Account>
) {
    navigation<SettingsScreens.Accounts>(startDestination = AccountsSettingsScreens.EditAccounts) {
        composable<AccountsSettingsScreens.EditAccounts> { backStack ->
            val viewModel = backStack.sharedViewModel<EditAccountsViewModel>(
                navController = navController,
                factory = EditAccountsViewModelFactory(accountList)
            )
            val accountListState by viewModel.accountListState.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            SetupAccountsScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = appUiSettings.isSetUp,
                accountsList = accountListState,
                appTheme = appUiSettings.appTheme,
                navigateToEditAccountScreen = { orderNum ->
                    navController.navigate(AccountsSettingsScreens.EditAccount(orderNum))
                },
                onAddNewAccount = {
                    viewModel.addNewDefaultAccount()
                },
                swapAccounts = viewModel::swapAccounts,
                onResetButton = {
                    viewModel.resetAccountsList()
                },
                onSaveButton = { newAccountsList ->
                    coroutineScope.launch {
                        appViewModel.saveAccountsToDb(newAccountsList.toAccountEntityList())
                        if (appUiSettings.isSetUp) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(SettingsScreens.Categories)
                        }
                    }
                }
            )
        }
        composable<AccountsSettingsScreens.EditAccount> { backStack ->
            val accountOrderNum = backStack.toRoute<AccountsSettingsScreens.EditAccount>().orderNum

            val editAccountsViewModel = backStack.sharedViewModel<EditAccountsViewModel>(navController)
            val showDeleteAccountButton by editAccountsViewModel.showDeleteAccountButton.collectAsState()
            val account = editAccountsViewModel.getAccountByOrderNum(accountOrderNum)
            val coroutineScope = rememberCoroutineScope()

            val editAccountViewModel = backStack.sharedViewModel<EditAccountViewModel>(
                navController = navController,
                factory = EditAccountViewModelFactory(
                    uiState = account?.toEditAccountUiState() ?: EditAccountUiState()
                )
            )
            val editAccountUiState by editAccountViewModel.uiState.collectAsStateWithLifecycle()
            account?.let {
                LaunchedEffect(it.id) {
                    if (editAccountUiState.id != account.id) {
                        editAccountViewModel.applyAccountData(it)
                    }
                }
            }

            EditAccountScreen(
                scaffoldPadding = scaffoldPadding,
                uiState = editAccountUiState,
                appTheme = appUiSettings.appTheme,
                showDeleteAccountButton = showDeleteAccountButton,
                onColorChange = editAccountViewModel::changeColor,
                onNameChange = editAccountViewModel::changeName,
                onNavigateToEditAccountCurrencyScreen = {
                    navController.navigate(
                        AccountsSettingsScreens.EditAccountCurrency(editAccountUiState.currency)
                    )
                },
                onBalanceChange = editAccountViewModel::changeBalance,
                onHideChange = editAccountViewModel::changeHide,
                onHideBalanceChange = editAccountViewModel::changeHideBalance,
                onWithoutBalanceChange = editAccountViewModel::changeWithoutBalance,
                onDeleteButton = { idToDelete: Int ->
                    navController.popBackStack()
                    editAccountsViewModel.deleteAccountById(idToDelete)?.let {
                        coroutineScope.launch {
                            appViewModel.deleteAccountWithItsRecords(
                                accountId = idToDelete,
                                updatedAccountList = it.toAccountEntityList()
                            )
                        }
                    }
                },
                onSaveButton = {
                    if (account != null) {
                        editAccountsViewModel.saveAccountData(editAccountUiState.toAccount())
                    }
                    navController.popBackStack()
                }
            )
        }
        composable<AccountsSettingsScreens.EditAccountCurrency> { backStack ->
            val currency = backStack.toRoute<AccountsSettingsScreens.EditAccountCurrency>().currency

            val editAccountViewModel = backStack.sharedViewModel<EditAccountViewModel>(navController)
            val currencyPickerViewModel = viewModel<CurrencyPickerViewModel>(
                factory = CurrencyPickerViewModelFactory(
                    selectedCurrency = currency
                )
            )

            CurrencyPickerScreen(
                viewModel = currencyPickerViewModel,
                scaffoldPadding = scaffoldPadding,
                onSaveButtonClick = { selectedCurrency ->
                    editAccountViewModel.changeCurrency(selectedCurrency)
                    navController.popBackStack()
                }
            )
        }
    }
}

fun NavGraphBuilder.categoriesGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    categoriesWithSubcategories: CategoriesWithSubcategories
) {
    navigation<SettingsScreens.Categories>(
        startDestination = CategoriesSettingsScreens.EditCategories
    ) {
        composable<CategoriesSettingsScreens.EditCategories> { backStack ->
            val viewModel = backStack.sharedViewModel<SetupCategoriesViewModel>(
                navController = navController,
                factory = SetupCategoriesViewModelFactory(
                    categoriesWithSubcategories = categoriesWithSubcategories
                        .takeIf { it.expense.isNotEmpty() && it.income.isNotEmpty() }
                        ?: DefaultCategoriesPackage(LocalContext.current).getDefaultCategories()
                )
            )
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            LaunchedEffect(true) {
                if (uiState.subcategoryList.isNotEmpty()) viewModel.clearSubcategoryList()
            }

            SetupCategoriesScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = appUiSettings.isSetUp,
                appTheme = appUiSettings.appTheme,
                uiState = uiState,
                onShowCategoriesByType = viewModel::changeCategoryTypeToShow,
                onNavigateToEditSubcategoryListScreen = { category: CategoryWithSubcategories ->
                    viewModel.applySubcategoryListToEdit(category)
                    navController.navigate(CategoriesSettingsScreens.EditSubcategories)
                },
                onNavigateToEditCategoryScreen = { category ->
                    viewModel.applyCategoryToEdit(category)
                    navController.navigate(CategoriesSettingsScreens.EditCategory)
                },
                onSwapCategories = viewModel::swapParentCategories,
                onAddNewCategory = {
                    viewModel.addNewParentCategory(context)
                },
                onResetButton = viewModel::reapplyCategoryLists,
                onSaveAndFinishSetupButton = {
                    coroutineScope.launch {
                        appViewModel.saveCategoriesToDb(
                            viewModel.getAllCategories().toCategoryEntityList()
                        )
                        if (appUiSettings.isSetUp) {
                            navController.popBackStack()
                        } else {
                            appViewModel.preFinishSetup()
                        }
                    }
                }
            )
        }
        composable<CategoriesSettingsScreens.EditSubcategories> {
            val viewModel = it.sharedViewModel<SetupCategoriesViewModel>(navController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val context = LocalContext.current

            EditSubcategoryListScreen(
                scaffoldPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                categoryWithSubcategories = uiState.categoryWithSubcategories,
                onSaveButton = {
                    viewModel.saveSubcategoryList()
                    navController.popBackStack()
                },
                onNavigateToEditCategoryScreen = { category ->
                    viewModel.applyCategoryToEdit(category)
                    navController.navigate(CategoriesSettingsScreens.EditCategory)
                },
                onSwapCategories = viewModel::swapSubcategories,
                onAddNewSubcategory = {
                    viewModel.addNewSubcategory(context)
                }
            )
        }
        composable<CategoriesSettingsScreens.EditCategory> {
            val viewModel = it.sharedViewModel<SetupCategoriesViewModel>(navController)
            val uiState by viewModel.editCategoryUiState.collectAsStateWithLifecycle()

            uiState.category?.let { category ->
                EditCategoryScreen(
                    scaffoldPadding = scaffoldPadding,
                    appTheme = appUiSettings.appTheme,
                    category = category,
                    showDeleteButton = uiState.showDeleteCategoryButton,
                    allowSaving = uiState.allowSaving,
                    onNameChange = viewModel::onCategoryNameChange,
                    onCategoryColorChange = viewModel::onCategoryColorChange,
                    onIconChange = viewModel::onCategoryIconChange,
                    onDeleteButton = {
                        viewModel.deleteCategory()
                        navController.popBackStack()
                    },
                    onSaveButton = {
                        viewModel.saveEditedCategory()
                        navController.popBackStack()
                    },
                    categoryIconList = CategoryPossibleIcons().asList()
                )
            } ?: Text(text = stringResource(R.string.category_not_found))
        }
    }
}

fun NavGraphBuilder.categoryCollectionsGraph(
    navController: NavHostController,
    appViewModel: AppViewModel,
    categoriesWithSubcategories: CategoriesWithSubcategories,
    categoryCollectionsWithIds: CategoryCollectionsWithIds
) {
    navigation<SettingsScreens.CategoryCollections>(
        startDestination = CategoryCollectionsSettingsScreens.EditCategoryCollections
    ) {
        composable<CategoryCollectionsSettingsScreens.EditCategoryCollections> { backStack ->

            val collectionsViewModel = backStack.sharedViewModel<CategoryCollectionsViewModel>(
                navController = navController,
                factory = CategoryCollectionsViewModelFactory(
                    categoryList = categoriesWithSubcategories.concatenateAsCategoryList(),
                    collectionsWithIds = categoryCollectionsWithIds
                )
            )
            val editCollectionViewModel = backStack
                .sharedViewModel<EditCategoryCollectionViewModel>(
                    navController = navController,
                    factory = EditCategoryCollectionViewModelFactory(
                        categoriesWithSubcategories = categoriesWithSubcategories
                    )
                )

            val collectionListByType by collectionsViewModel
                .collectionsWithCategoriesByType.collectAsStateWithLifecycle()
            val categoryCollectionType by collectionsViewModel
                .collectionType.collectAsStateWithLifecycle()

            val coroutineScope = rememberCoroutineScope()

            SetupCategoryCollectionsScreen(
                collectionsWithCategories = collectionListByType,
                collectionType = categoryCollectionType,
                onCategoryTypeChange = collectionsViewModel::changeCategoryType,
                onNavigateToEditCollectionScreen = { collectionOrNull ->
                    editCollectionViewModel.applyCollection(
                        collection = collectionOrNull ?: collectionsViewModel.getNewCollection()
                    )
                    navController.navigate(
                        CategoryCollectionsSettingsScreens.EditCategoryCollection
                    )
                },
                onSaveCollectionsButton = {
                    coroutineScope.launch {
                        appViewModel.saveCategoryCollectionsToDb(
                            collectionsViewModel.getAllCollections().toCollectionsWithIds()
                        )
                        navController.popBackStack()
                    }
                }
//                onSwapCategories = viewModel::swapParentCategories
            )
        }
        composable<CategoryCollectionsSettingsScreens.EditCategoryCollection> { backStack ->

            val collectionsViewModel = backStack.sharedViewModel<CategoryCollectionsViewModel>(
                navController = navController
            )
            val editCollectionViewModel = backStack
                .sharedViewModel<EditCategoryCollectionViewModel>(
                    navController = navController,
                    factory = EditCategoryCollectionViewModelFactory(
                        categoriesWithSubcategories = categoriesWithSubcategories
                    )
                )

            val collectionUiState by editCollectionViewModel
                .collectionUiState.collectAsStateWithLifecycle()
            val editingCategoriesWithSubcategories by editCollectionViewModel
                .editingCategoriesWithSubcategories.collectAsStateWithLifecycle()
            val expandedCategory by editCollectionViewModel
                .expandedCategory.collectAsStateWithLifecycle()
            val allowSaving by editCollectionViewModel
                .allowSaving.collectAsStateWithLifecycle()

            EditCategoryCollectionScreen(
                collection = collectionUiState,
                editingCategoriesWithSubcategories = editingCategoriesWithSubcategories,
                expandedCategory = expandedCategory,
                allowDeleting = editCollectionViewModel.allowDeleting.value,
                allowSaving = allowSaving,
                onNameChange = editCollectionViewModel::changeName,
                onCheckedChange = editCollectionViewModel::inverseCheckedCategoryState,
                onExpandedChange = editCollectionViewModel::inverseExpandedState,
                onDeleteButton = {
                    collectionsViewModel.deleteCollection(collectionUiState)
                    navController.popBackStack()
                },
                onSaveButton = {
                    collectionsViewModel.saveEditingCollection(
                        editingCollection = editCollectionViewModel.getCollection()
                    )
                    navController.popBackStack()
                }
            )
        }
    }
}
