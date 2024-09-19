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
import com.ataglance.walletglance.budget.presentation.screen.BudgetStatisticsScreen
import com.ataglance.walletglance.budget.presentation.screen.BudgetsScreen
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetStatisticsViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetStatisticsViewModelFactory
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.category.presentation.screen.CategoryStatisticsScreen
import com.ataglance.walletglance.category.presentation.viewmodel.CategoryStatisticsViewModel
import com.ataglance.walletglance.category.presentation.viewmodel.CategoryStatisticsViewModelFactory
import com.ataglance.walletglance.categoryCollection.navigation.CategoryCollectionsSettingsScreens
import com.ataglance.walletglance.core.domain.app.AppUiSettings
import com.ataglance.walletglance.core.domain.app.AppUiState
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
import com.ataglance.walletglance.personalization.presentation.containers.WidgetsSettingsBottomSheetContainer
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel
import com.ataglance.walletglance.record.presentation.screen.RecordsScreen
import com.ataglance.walletglance.record.presentation.viewmodel.RecordsViewModel
import com.ataglance.walletglance.record.presentation.viewmodel.RecordsViewModelFactory
import com.ataglance.walletglance.recordCreation.presentation.screen.RecordCreationScreen
import com.ataglance.walletglance.recordCreation.presentation.screen.TransferCreationScreen
import com.ataglance.walletglance.recordCreation.presentation.viewmodel.RecordCreationViewModel
import com.ataglance.walletglance.recordCreation.presentation.viewmodel.RecordCreationViewModelFactory
import com.ataglance.walletglance.recordCreation.presentation.viewmodel.TransferCreationViewModel
import com.ataglance.walletglance.recordCreation.presentation.viewmodel.TransferCreationViewModelFactory
import com.ataglance.walletglance.recordCreation.utils.getRecordDraft
import com.ataglance.walletglance.recordCreation.utils.getTransferDraft
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
    personalizationViewModel: PersonalizationViewModel,
    appUiSettings: AppUiSettings,
    themeUiState: ThemeUiState,
    appUiState: AppUiState,
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
                scaffoldPadding = scaffoldPadding,
                isAppThemeSetUp = appUiSettings.appTheme != null,
                accountsUiState = appUiState.accountsUiState,
                onTopBarAccountClick = appViewModel::applyActiveAccountByOrderNum,
                dateRangeWithEnum = appUiState.dateRangeMenuUiState.dateRangeWithEnum,
                onDateRangeChange = appViewModel::selectDateRange,
                isCustomDateRangeWindowOpened = openCustomDateRangeWindow,
                onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                widgetsUiState = widgetsUiState,
                onChangeHideActiveAccountBalance = appViewModel::onChangeHideActiveAccountBalance,
                onWidgetSettingsButtonClick = personalizationViewModel::openWidgetSettings,
                onNavigateToScreenMovingTowardsLeft = { screen ->
                    navViewModel.navigateToScreenMovingTowardsLeft(navController, screen)
                },
                widgetSettingsBottomSheets = {
                    WidgetsSettingsBottomSheetContainer(
                        personalizationViewModel = personalizationViewModel,
                        budgetsByType = appUiState.budgetsByType,
                        budgetsOnWidget = widgetsUiState.budgetsOnWidget
                    )
                }
            )
        }
        composable<MainScreens.Records> {
            val defaultCollectionName = stringResource(R.string.all_categories)

            val viewModel = viewModel<RecordsViewModel>(
                factory = RecordsViewModelFactory(
                    categoryCollections = appUiState.categoryCollectionsUiState
                        .appendDefaultCollection(name = defaultCollectionName),
                    recordsFilteredByDateAndAccount = widgetsUiState.recordStacksByDateAndAccount
                )
            )
            LaunchedEffect(widgetsUiState.compactRecordStacksByDateAndAccount) {
                viewModel.setRecordsByDateAndAccount(
                    widgetsUiState.compactRecordStacksByDateAndAccount
                )
            }
            LaunchedEffect(appUiState.categoryCollectionsUiState) {
                viewModel.setCategoryCollections(
                    appUiState.categoryCollectionsUiState
                        .appendDefaultCollection(name = defaultCollectionName)
                )
            }

            val collectionType by viewModel.collectionType.collectAsStateWithLifecycle()
            val filteredRecords by viewModel.recordsByDateAccountAndCollection
                .collectAsStateWithLifecycle()
            val collectionList by viewModel.currentCollectionList.collectAsStateWithLifecycle()
            val selectedCollection by viewModel.selectedCollection.collectAsStateWithLifecycle()

            RecordsScreen(
                scaffoldAppScreenPadding = scaffoldPadding,
                accountList = appUiState.accountsUiState.accountList,
                onAccountClick = appViewModel::applyActiveAccountByOrderNum,
                currentDateRangeEnum = appUiState.dateRangeMenuUiState.dateRangeWithEnum.enum,
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
                    categoriesWithSubcategories = appUiState.categoriesWithSubcategories,
                    categoryCollections = appUiState.categoryCollectionsUiState
                        .appendDefaultCollection(name = defaultCollectionName),
                    recordsFilteredByDateAndAccount = widgetsUiState.recordStacksByDateAndAccount,
                    categoryStatisticsLists = widgetsUiState.categoryStatisticsLists,
                    parentCategoryId = parentCategoryId
                )
            )
            LaunchedEffect(widgetsUiState.categoryStatisticsLists) {
                viewModel.setCategoryStatisticsByAccountAndDate(widgetsUiState.categoryStatisticsLists)
            }
            LaunchedEffect(widgetsUiState.recordStacksByDateAndAccount) {
                viewModel.setRecordsFilteredByDateAndAccount(
                    recordList = widgetsUiState.recordStacksByDateAndAccount
                )
            }
            LaunchedEffect(appUiState.categoryCollectionsUiState) {
                viewModel.setCategoryCollections(
                    appUiState.categoryCollectionsUiState
                        .appendDefaultCollection(name = defaultCollectionName)
                )
            }
            LaunchedEffect(
                key1 = appUiState.dateRangeMenuUiState.dateRangeWithEnum.enum,
                key2 = appUiState.accountsUiState.accountList
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
                accountList = appUiState.accountsUiState.accountList,
                onAccountClick = appViewModel::applyActiveAccountByOrderNum,
                currentDateRangeEnum = appUiState.dateRangeMenuUiState.dateRangeWithEnum.enum,
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
                budgetsByType = appUiState.budgetsByType,
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
                derivedStateOf { appUiState.budgetsByType.findById(budgetId) }
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
                derivedStateOf { appUiState.accountsUiState.filterByBudget(budget) }
            }

            (budget to columnChartDataUiState).letIfNoneIsNull { (budget, chartUiState) ->
                BudgetStatisticsScreen(
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

            val initialCategoryWithSubcategoryByType = appViewModel.getLastUsedRecordCategories()

            val viewModel = viewModel<RecordCreationViewModel>(
                factory = RecordCreationViewModelFactory(
                    initialCategoryWithSubcategoryByType = initialCategoryWithSubcategoryByType,
                    recordDraft = appUiState.recordStackListByDate.getRecordDraft(
                        isNew = isNew,
                        recordNum = recordNum,
                        accountsUiState = appUiState.accountsUiState,
                        initialCategoryWithSubcategory = initialCategoryWithSubcategoryByType
                            .getByType(CategoryType.Expense)
                    )
                )
            )

            val recordDraftGeneral by viewModel.recordDraftGeneral.collectAsStateWithLifecycle()
            val recordDraftItems by viewModel.recordDraftItems.collectAsStateWithLifecycle()
            val savingIsAllowed by viewModel.savingIsAllowed.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            RecordCreationScreen(
                recordDraftGeneral = recordDraftGeneral,
                recordDraftItems = recordDraftItems,
                savingIsAllowed = savingIsAllowed,
                accountList = appUiState.accountsUiState.accountList,
                categoriesWithSubcategories = appUiState.categoriesWithSubcategories,
                onSelectCategoryType = viewModel::selectCategoryType,
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
                    accountList = appUiState.accountsUiState.accountList,
                    transferDraft = appUiState.recordStackListByDate.getTransferDraft(
                        isNew = isNew,
                        recordNum = recordNum,
                        accountsUiState = appUiState.accountsUiState
                    )
                )
            )

            val transferDraft by viewModel.transferDraft.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            TransferCreationScreen(
                transferDraft = transferDraft,
                accountList = appUiState.accountsUiState.accountList,
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
            navigationButtonList = appUiState.navigationButtonList,
            appViewModel = appViewModel,
            appUiSettings = appUiSettings,
            themeUiState = themeUiState,
            accountList = appUiState.accountsUiState.accountList,
            categoriesWithSubcategories = appUiState.categoriesWithSubcategories,
            categoryCollectionsUiState = appUiState.categoryCollectionsUiState,
            budgetsByType = appUiState.budgetsByType,
            personalizationViewModel = personalizationViewModel,
            widgetNamesList = widgetsUiState.widgetNamesList
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