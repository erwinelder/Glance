package com.ataglance.walletglance.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.AccountsUiState
import com.ataglance.walletglance.budget.domain.BudgetsByType
import com.ataglance.walletglance.budget.presentation.screen.BudgetStatisticsScreen
import com.ataglance.walletglance.budget.presentation.screen.BudgetsScreen
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetStatisticsViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetStatisticsViewModelFactory
import com.ataglance.walletglance.category.domain.CategoriesWithSubcategories
import com.ataglance.walletglance.category.presentation.screen.CategoriesStatisticsScreen
import com.ataglance.walletglance.category.presentation.viewmodel.CategoryStatisticsViewModel
import com.ataglance.walletglance.category.presentation.viewmodel.CategoryStatisticsViewModelFactory
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionsWithIds
import com.ataglance.walletglance.categoryCollection.navigation.CategoryCollectionsSettingsScreens
import com.ataglance.walletglance.core.domain.app.AppUiSettings
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState
import com.ataglance.walletglance.core.domain.statistics.ColumnChartUiState
import com.ataglance.walletglance.core.domain.widgets.WidgetsUiState
import com.ataglance.walletglance.core.presentation.animation.screenEnterTransition
import com.ataglance.walletglance.core.presentation.animation.screenExitTransition
import com.ataglance.walletglance.core.presentation.screen.HomeScreen
import com.ataglance.walletglance.core.presentation.screen.SetupFinishScreen
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.utils.getPrevDateRanges
import com.ataglance.walletglance.core.utils.takeIfNoneIsNull
import com.ataglance.walletglance.makingRecord.domain.MakeRecordStatus
import com.ataglance.walletglance.makingRecord.domain.MakeRecordUiState
import com.ataglance.walletglance.makingRecord.presentation.screen.MakeRecordScreen
import com.ataglance.walletglance.makingRecord.presentation.screen.MakeTransferScreen
import com.ataglance.walletglance.makingRecord.presentation.viewmodel.MakeRecordViewModel
import com.ataglance.walletglance.makingRecord.presentation.viewmodel.MakeRecordViewModelFactory
import com.ataglance.walletglance.makingRecord.presentation.viewmodel.MakeTransferViewModel
import com.ataglance.walletglance.makingRecord.presentation.viewmodel.MakeTransferViewModelFactory
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.presentation.screen.RecordsScreen
import com.ataglance.walletglance.record.presentation.viewmodel.RecordsViewModel
import com.ataglance.walletglance.record.presentation.viewmodel.RecordsViewModelFactory
import com.ataglance.walletglance.record.utils.getMakeRecordStateAndUnitList
import com.ataglance.walletglance.record.utils.getMakeTransferState
import com.ataglance.walletglance.settings.domain.ThemeUiState
import com.ataglance.walletglance.settings.navigation.settingsGraph
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
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
    budgetsByType: BudgetsByType,
    widgetsUiState: WidgetsUiState,
    openCustomDateRangeWindow: Boolean,
    onCustomDateRangeButtonClick: () -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = appUiSettings.mainStartDestination,
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
                onDateRangeChange = appViewModel::selectDateRange,
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
            val defaultCollectionName = stringResource(R.string.all_categories)

            val viewModel = viewModel<RecordsViewModel>(
                factory = RecordsViewModelFactory(
                    categoryCollections = categoryCollectionsUiState.appendDefaultCollection(
                        name = defaultCollectionName
                    ),
                    recordsFilteredByDateAndAccount = widgetsUiState.recordsFilteredByDateAndAccount
                )
            )
            LaunchedEffect(widgetsUiState.recordsFilteredByDateAndAccount) {
                viewModel.setRecordsFilteredByDateAndAccount(
                    widgetsUiState.recordsFilteredByDateAndAccount
                )
            }
            LaunchedEffect(categoryCollectionsUiState) {
                viewModel.setCategoryCollections(
                    categoryCollectionsUiState.appendDefaultCollection(name = defaultCollectionName)
                )
            }

            val collectionType by viewModel.collectionType.collectAsStateWithLifecycle()
            val filteredRecords by viewModel
                .recordsFilteredByDateAccountAndCollection.collectAsStateWithLifecycle()
            val collectionList by viewModel.currentCollectionList.collectAsStateWithLifecycle()
            val selectedCollection by viewModel.selectedCollection.collectAsStateWithLifecycle()

            RecordsScreen(
                scaffoldAppScreenPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                accountList = accountsUiState.accountList,
                onAccountClick = { orderNum ->
                    appViewModel.applyActiveAccountByOrderNum(orderNum)
                },
                currentDateRangeEnum = dateRangeMenuUiState.dateRangeWithEnum.enum,
                isCustomDateRangeWindowOpened = openCustomDateRangeWindow,
                onDateRangeChange = appViewModel::selectDateRange,
                onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                collectionType = collectionType,
                filteredRecords = filteredRecords,
                collectionList = collectionList,
                selectedCollection = selectedCollection,
                onCollectionSelect = viewModel::selectCollection,
                onToggleCollectionType = viewModel::toggleCollectionType,
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
                },
                onNavigateToEditCollectionsScreen = {
                    changeMoveScreenTowardsLeft(true)
                    navController.navigate(
                        CategoryCollectionsSettingsScreens.EditCategoryCollections
                    ) {
                        launchSingleTop = true
                    }
                },
                onDimBackgroundChange = onDimBackgroundChange
            )
        }
        composable<MainScreens.CategoryStatistics> { backStack ->
            val parentCategoryId =
                backStack.toRoute<MainScreens.CategoryStatistics>().parentCategoryId
            val defaultCollectionName = stringResource(R.string.all_categories)

            val viewModel = viewModel<CategoryStatisticsViewModel>(
                factory = CategoryStatisticsViewModelFactory(
                    categoriesWithSubcategories = categoriesWithSubcategories,
                    categoryCollections = categoryCollectionsUiState.appendDefaultCollection(
                        name = defaultCollectionName
                    ),
                    recordsFilteredByDateAndAccount = widgetsUiState.recordsFilteredByDateAndAccount,
                    categoryStatisticsLists = widgetsUiState.categoryStatisticsLists,
                    parentCategoryId = parentCategoryId
                )
            )
            LaunchedEffect(widgetsUiState.categoryStatisticsLists) {
                viewModel.setCategoryStatisticsLists(widgetsUiState.categoryStatisticsLists)
            }
            LaunchedEffect(widgetsUiState.recordsFilteredByDateAndAccount) {
                viewModel.setRecordsFilteredByDateAndAccount(
                    widgetsUiState.recordsFilteredByDateAndAccount
                )
            }
            LaunchedEffect(categoryCollectionsUiState) {
                viewModel.setCategoryCollections(
                    categoryCollectionsUiState.appendDefaultCollection(name = defaultCollectionName)
                )
            }
            LaunchedEffect(
                dateRangeMenuUiState.dateRangeWithEnum.enum,
                accountsUiState.accountList
            ) {
                viewModel.clearParentCategory()
            }
            LaunchedEffect(true) {
                viewModel.setParentCategory()
                viewModel.clearParentCategoryId()
            }

            CategoriesStatisticsScreen(
                scaffoldAppScreenPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                accountList = accountsUiState.accountList,
                onAccountClick = { orderNum ->
                    appViewModel.applyActiveAccountByOrderNum(orderNum)
                },
                currentDateRangeEnum = dateRangeMenuUiState.dateRangeWithEnum.enum,
                isCustomDateRangeWindowOpened = openCustomDateRangeWindow,
                onDateRangeChange = appViewModel::selectDateRange,
                onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                viewModel = viewModel,
                onNavigateToEditCollectionsScreen = {
                    changeMoveScreenTowardsLeft(true)
                    navController.navigate(
                        CategoryCollectionsSettingsScreens.EditCategoryCollections
                    ) {
                        launchSingleTop = true
                    }
                },
                onDimBackgroundChange = onDimBackgroundChange
            )
        }
        composable<MainScreens.Budgets> {
            BudgetsScreen(
                scaffoldPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                budgetsByType = budgetsByType,
                onBudgetClick = {

                }
            )
        }
        composable<MainScreens.BudgetStatistics> { backStack ->
            val budgetId = backStack.toRoute<MainScreens.BudgetStatistics>().id
            val budget by remember {
                derivedStateOf { budgetsByType.findById(budgetId) }
            }
            val context = LocalContext.current

            val budgetStatisticsViewModel = viewModel<BudgetStatisticsViewModel>(
                factory = BudgetStatisticsViewModelFactory(
                    budget = budget,
                    usedAmountByRangeList = budget?.let {
                        appViewModel.fetchBudgetsTotalUsedAmountsByDateRanges(
                            budget = it,
                            dateRanges = it.repeatingPeriod.getPrevDateRanges()
                        )
                    } ?: emptyFlow()
                )
            )

            val budgetsTotalAmountsByRanges by budgetStatisticsViewModel
                .budgetsTotalAmountsByRanges.collectAsState()
            val columnChartDataUiState by derivedStateOf {
                budget?.let {
                    ColumnChartUiState.createAsBudgetStatistics(
                        totalAmountsByRanges = budgetsTotalAmountsByRanges,
                        rowsCount = 5,
                        repeatingPeriod = it.repeatingPeriod,
                        context = context
                    )
                }
            }
            val budgetAccounts by remember {
                derivedStateOf { accountsUiState.filterByBudget(budget) }
            }

            (budget to columnChartDataUiState).takeIfNoneIsNull()?.let { (budget, chartUiState) ->
                BudgetStatisticsScreen(
                    appTheme = appUiSettings.appTheme,
                    budget = budget,
                    columnChartUiState = chartUiState,
                    budgetAccounts = budgetAccounts,
                    onBackButtonClick = {
                        navController.popBackStack()
                    }
                )
            } ?: Text(text = "Budget not found")
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
                onDimBackgroundChange = onDimBackgroundChange
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
            categoryCollectionsUiState = categoryCollectionsUiState,
            budgetsByType = budgetsByType
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