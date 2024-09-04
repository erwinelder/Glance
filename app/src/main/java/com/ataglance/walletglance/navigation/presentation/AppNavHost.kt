package com.ataglance.walletglance.navigation.presentation

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
import com.ataglance.walletglance.category.presentation.screen.CategoryStatisticsScreen
import com.ataglance.walletglance.category.presentation.viewmodel.CategoryStatisticsViewModel
import com.ataglance.walletglance.category.presentation.viewmodel.CategoryStatisticsViewModelFactory
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.navigation.CategoryCollectionsSettingsScreens
import com.ataglance.walletglance.core.domain.app.AppUiSettings
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState
import com.ataglance.walletglance.core.domain.statistics.ColumnChartUiState
import com.ataglance.walletglance.core.domain.widgets.WidgetsUiState
import com.ataglance.walletglance.core.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.animation.screenEnterTransition
import com.ataglance.walletglance.core.presentation.animation.screenExitTransition
import com.ataglance.walletglance.core.presentation.screen.HomeScreen
import com.ataglance.walletglance.core.presentation.screen.SetupFinishScreen
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.utils.getPrevDateRanges
import com.ataglance.walletglance.core.utils.letIfNoneIsNull
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.presentation.screen.RecordsScreen
import com.ataglance.walletglance.record.presentation.viewmodel.RecordsViewModel
import com.ataglance.walletglance.record.presentation.viewmodel.RecordsViewModelFactory
import com.ataglance.walletglance.record.utils.getTransferDraft
import com.ataglance.walletglance.recordCreation.presentation.screen.RecordCreationScreen
import com.ataglance.walletglance.recordCreation.presentation.screen.TransferCreationScreen
import com.ataglance.walletglance.recordCreation.presentation.viewmodel.RecordCreationViewModel
import com.ataglance.walletglance.recordCreation.presentation.viewmodel.RecordCreationViewModelFactory
import com.ataglance.walletglance.recordCreation.presentation.viewmodel.TransferCreationViewModel
import com.ataglance.walletglance.recordCreation.presentation.viewmodel.TransferCreationViewModelFactory
import com.ataglance.walletglance.recordCreation.utils.getRecordDraft
import com.ataglance.walletglance.settings.domain.ThemeUiState
import com.ataglance.walletglance.settings.navigation.settingsGraph
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    navViewModel: NavigationViewModel,
    moveScreenTowardsLeft: Boolean,
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    themeUiState: ThemeUiState,
    accountsUiState: AccountsUiState,
    categoriesWithSubcategories: CategoriesWithSubcategories,
    categoryCollectionsUiState: CategoryCollectionsWithIdsByType,
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
                onTopBarAccountClick = appViewModel::applyActiveAccountByOrderNum,
                isCustomDateRangeWindowOpened = openCustomDateRangeWindow,
                onNavigateToScreenMovingTowardsLeft = { screen ->
                    navViewModel.navigateToScreenMovingTowardsLeft(navController, screen)
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
                onAccountClick = appViewModel::applyActiveAccountByOrderNum,
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
                onNavigateToScreenMovingTowardsLeft = { screen ->
                    navViewModel.navigateToScreenMovingTowardsLeft(navController, screen)
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
                viewModel.setCategoryStatisticsByAccountAndDate(widgetsUiState.categoryStatisticsLists)
            }
            LaunchedEffect(widgetsUiState.recordsFilteredByDateAndAccount) {
                viewModel.setRecordsFilteredByDateAndAccount(
                    recordList = widgetsUiState.recordsFilteredByDateAndAccount
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
                viewModel.clearParentCategoryStatistics()
            }
            LaunchedEffect(true) {
                viewModel.setParentCategoryStatistics()
                viewModel.clearParentCategoryId()
            }

            val parentCategory by viewModel.parentCategoryStatistics.collectAsStateWithLifecycle()
            val categoryStatisticsList by viewModel.categoryStatisticsList
                .collectAsStateWithLifecycle()
            val categoryType by viewModel.categoryType.collectAsStateWithLifecycle()
            val collectionList by viewModel.currentCollectionList.collectAsStateWithLifecycle()
            val selectedCollection by viewModel.selectedCollection.collectAsStateWithLifecycle()

            CategoryStatisticsScreen(
                scaffoldAppScreenPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                accountList = accountsUiState.accountList,
                onAccountClick = appViewModel::applyActiveAccountByOrderNum,
                currentDateRangeEnum = dateRangeMenuUiState.dateRangeWithEnum.enum,
                isCustomDateRangeWindowOpened = openCustomDateRangeWindow,
                onDateRangeChange = appViewModel::selectDateRange,
                onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                parentCategory = parentCategory,
                categoryStatisticsList = categoryStatisticsList,
                currentCategoryType = categoryType,
                collectionList = collectionList,
                selectedCollection = selectedCollection,
                onCollectionSelect = viewModel::selectCollection,
                onNavigateToEditCollectionsScreen = {
                    navViewModel.navigateToScreenMovingTowardsLeft(
                        navController = navController,
                        screen = CategoryCollectionsSettingsScreens.EditCategoryCollections
                    )
                },
                onSetCategoryType = viewModel::setCategoryType,
                onSetParentCategory = viewModel::setParentCategoryStatistics,
                onClearParentCategory = viewModel::clearParentCategoryStatistics,
                onDimBackgroundChange = onDimBackgroundChange
            )
        }
        composable<MainScreens.Budgets> {
            BudgetsScreen(
                screenPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                budgetsByType = budgetsByType,
                onBudgetClick = { budget ->
                    navViewModel.navigateToScreenMovingTowardsLeft(
                        navController = navController,
                        screen = MainScreens.BudgetStatistics(budget.id)
                    )
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

            val budgetsTotalAmountsByRanges by budgetStatisticsViewModel.budgetsTotalAmountsByRanges
                .collectAsState()
            val columnChartDataUiState by remember {
                derivedStateOf {
                    budget?.let {
                        ColumnChartUiState.createAsBudgetStatistics(
                            totalAmountsByRanges = budgetsTotalAmountsByRanges,
                            rowsCount = 5,
                            repeatingPeriod = it.repeatingPeriod,
                            context = context
                        )
                    }
                }
            }
            val budgetAccounts by remember {
                derivedStateOf { accountsUiState.filterByBudget(budget) }
            }

            (budget to columnChartDataUiState).letIfNoneIsNull { (budget, chartUiState) ->
                BudgetStatisticsScreen(
                    appTheme = appUiSettings.appTheme,
                    budget = budget,
                    columnChartUiState = chartUiState,
                    budgetAccounts = budgetAccounts,
                    onBackButtonClick = navController::popBackStack
                )
            } ?: Text(text = "Budget not found")
        }
        composable<MainScreens.RecordCreation>(
            enterTransition = { screenEnterTransition() },
            popEnterTransition = { screenEnterTransition(!moveScreenTowardsLeft) },
            exitTransition = { screenExitTransition(moveScreenTowardsLeft) },
            popExitTransition = { screenExitTransition(false) }
        ) { backStack ->
            val isNew = backStack.toRoute<MainScreens.RecordCreation>().isNew
            val recordNum = backStack.toRoute<MainScreens.RecordCreation>().recordNum

            val initialCategoryWithSubcategory = accountsUiState.activeAccount?.id
                ?.takeIf { isNew }
                ?.let { appViewModel.getLastRecordCategory(accountId = it) }
            val recordDraft = recordStackList.getRecordDraft(
                isNew = isNew,
                recordNum = recordNum,
                accountsUiState = accountsUiState,
                initialCategoryWithSubcategory = initialCategoryWithSubcategory
            )

            val viewModel = viewModel<RecordCreationViewModel>(
                factory = RecordCreationViewModelFactory(recordDraft = recordDraft)
            )

            val recordDraftGeneral by viewModel.recordDraftGeneral.collectAsStateWithLifecycle()
            val recordDraftItems by viewModel.recordDraftItems.collectAsStateWithLifecycle()
            val savingIsAllowed by viewModel.savingIsAllowed.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            RecordCreationScreen(
                appTheme = appUiSettings.appTheme,
                recordDraftGeneral = recordDraftGeneral,
                recordDraftItems = recordDraftItems,
                savingIsAllowed = savingIsAllowed,
                accountList = accountsUiState.accountList,
                categoriesWithSubcategories = categoriesWithSubcategories,
                onSelectCategoryType = { type ->
                    viewModel.selectCategoryType(type, categoriesWithSubcategories)
                },
                onNavigateToTransferCreationScreen = {
                    navViewModel.navigateToScreen(
                        navController = navController,
                        screen = MainScreens.TransferCreation(
                            isNew = true, recordNum = appUiSettings.nextRecordNum()
                        )
                    )
                },
                onIncludeInBudgetsChange = viewModel::changeIncludeInBudgets,
                onSelectDate = viewModel::selectDate,
                onSelectTime = viewModel::selectTime,
                onToggleAccounts = viewModel::toggleSelectedAccount,
                onSelectAccount = viewModel::selectAccount,
                onDimBackgroundChange = onDimBackgroundChange,
                onAmountChange = viewModel::changeAmount,
                onSelectCategory = viewModel::selectCategory,
                onNoteChange = viewModel::changeNote,
                onQuantityChange = viewModel::changeQuantity,
                onSwapItems = viewModel::swapDraftItems,
                onDeleteItem = viewModel::deleteDraftItem,
                onCollapsedChange = viewModel::changeCollapsed,
                onAddDraftItemButton = viewModel::addNewDraftItem,
                onSaveButton = {
                    coroutineScope.launch {
                        appViewModel.saveRecord(viewModel.getRecordDraft())
                    }
                    navController.popBackStack()
                },
                onRepeatButton = {
                    coroutineScope.launch {
                        appViewModel.repeatRecord(viewModel.getRecordDraft())
                    }
                    navController.popBackStack()
                },
                onDeleteButton = {
                    coroutineScope.launch {
                        appViewModel.deleteRecord(recordNum)
                    }
                    navController.popBackStack()
                }
            )
        }
        composable<MainScreens.TransferCreation>(
            enterTransition = { screenEnterTransition() },
            popExitTransition = { screenExitTransition(false) }
        ) { backStack ->
            val isNew = backStack.toRoute<MainScreens.TransferCreation>().isNew
            val recordNum = backStack.toRoute<MainScreens.TransferCreation>().recordNum

            val viewModel = viewModel<TransferCreationViewModel>(
                factory = TransferCreationViewModelFactory(
                    accountList = accountsUiState.accountList,
                    transferDraft = recordStackList
                        .getTransferDraft(
                            isNew = isNew,
                            recordNum = recordNum.takeUnless { it == 0 }
                                ?: appUiSettings.nextRecordNum(),
                            accountsUiState = accountsUiState
                        )
                )
            )

            val transferDraft by viewModel.transferDraft.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            TransferCreationScreen(
                appTheme = appUiSettings.appTheme,
                transferDraft = transferDraft,
                accountList = accountsUiState.accountList,
                onNavigateBack = navController::popBackStack,
                onSelectNewDate = viewModel::selectNewDate,
                onSelectNewTime = viewModel::selectNewTime,
                onSelectAnotherAccount = viewModel::selectAnotherAccount,
                onSelectAccount = viewModel::selectAccount,
                onRateChange = viewModel::changeRate,
                onAmountChange = viewModel::changeAmount,
                onSaveButton = {
                    coroutineScope.launch {
                        appViewModel.saveTransfer(viewModel.getTransferDraft())
                    }
                    navController.popBackStack(MainScreens.Home, false)
                },
                onRepeatButton = {
                    coroutineScope.launch {
                        appViewModel.repeatTransfer(viewModel.getTransferDraft())
                    }
                    navController.popBackStack(MainScreens.Home, false)
                },
                onDeleteButton = {
                    coroutineScope.launch {
                        appViewModel.deleteTransfer(viewModel.getSenderReceiverRecordNums())
                    }
                    navController.popBackStack(MainScreens.Home, false)
                }
            )
        }
        settingsGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            navViewModel = navViewModel,
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