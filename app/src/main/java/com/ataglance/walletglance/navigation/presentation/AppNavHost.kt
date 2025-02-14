package com.ataglance.walletglance.navigation.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.ataglance.walletglance.auth.domain.model.AuthController
import com.ataglance.walletglance.budget.presentation.containers.BudgetsOnWidgetSettingsBottomSheet
import com.ataglance.walletglance.budget.presentation.screen.BudgetStatisticsScreen
import com.ataglance.walletglance.budget.presentation.screen.BudgetsScreen
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetStatisticsViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetsOnWidgetSettingsViewModel
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetsViewModel
import com.ataglance.walletglance.category.presentation.screen.CategoryStatisticsScreen
import com.ataglance.walletglance.category.presentation.viewmodel.CategoryStatisticsViewModel
import com.ataglance.walletglance.category.presentation.viewmodel.CategoryStatisticsViewModelFactory
import com.ataglance.walletglance.categoryCollection.presentation.navigation.CategoryCollectionsSettingsScreens
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppUiState
import com.ataglance.walletglance.core.domain.statistics.ColumnChartUiState
import com.ataglance.walletglance.core.domain.widgets.WidgetsUiState
import com.ataglance.walletglance.core.presentation.animation.screenEnterTransition
import com.ataglance.walletglance.core.presentation.animation.screenExitTransition
import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.screen.HomeScreen
import com.ataglance.walletglance.core.presentation.screen.SetupFinishScreen
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel
import com.ataglance.walletglance.record.presentation.screen.RecordsScreen
import com.ataglance.walletglance.record.presentation.viewmodel.RecordsViewModel
import com.ataglance.walletglance.record.presentation.viewmodel.RecordsViewModelFactory
import com.ataglance.walletglance.recordCreation.presentation.screen.RecordCreationScreen
import com.ataglance.walletglance.recordCreation.presentation.screen.TransferCreationScreen
import com.ataglance.walletglance.recordCreation.presentation.viewmodel.RecordCreationViewModel
import com.ataglance.walletglance.recordCreation.presentation.viewmodel.TransferCreationViewModel
import com.ataglance.walletglance.settings.navigation.settingsGraph
import com.ataglance.walletglance.settings.presentation.model.ThemeUiState
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavHost(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    navViewModel: NavigationViewModel,
    appViewModel: AppViewModel,
    personalizationViewModel: PersonalizationViewModel,
    moveScreenTowardsLeft: Boolean,
    appConfiguration: AppConfiguration,
    themeUiState: ThemeUiState,
    appUiState: AppUiState,
    widgetsUiState: WidgetsUiState,
    openCustomDateRangeWindow: Boolean,
    onCustomDateRangeButtonClick: () -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit
) {
    val authController = koinInject<AuthController>()

    val budgetsOnWidgetSettingsViewModel = koinViewModel<BudgetsOnWidgetSettingsViewModel>()

    NavHost(
        navController = navController,
        startDestination = appConfiguration.mainStartDestination,
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
                isAppThemeSetUp = appConfiguration.appTheme != null,
                accountsAndActiveOne = appUiState.accountsAndActiveOne,
                onTopBarAccountClick = appViewModel::applyActiveAccountByOrderNum,
                dateRangeWithEnum = appUiState.dateRangeMenuUiState.dateRangeWithEnum,
                onDateRangeChange = appViewModel::selectDateRange,
                isCustomDateRangeWindowOpened = openCustomDateRangeWindow,
                onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                widgetsUiState = widgetsUiState,
                onChangeHideActiveAccountBalance = appViewModel::onChangeHideActiveAccountBalance,
                onWidgetSettingsButtonClick = budgetsOnWidgetSettingsViewModel::openWidgetSettings,
                onNavigateToScreenMovingTowardsLeft = { screen ->
                    navViewModel.navigateToScreenMovingTowardsLeft(navController, screen)
                },
                widgetSettingsBottomSheets = {
                    BudgetsOnWidgetSettingsBottomSheet(viewModel = budgetsOnWidgetSettingsViewModel)
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
                    appUiState.categoryCollectionsUiState.appendDefaultCollection(name = defaultCollectionName)
                )
            }

            val collectionType by viewModel.collectionType.collectAsStateWithLifecycle()
            val filteredRecords by viewModel.recordsByDateAccountAndCollection.collectAsStateWithLifecycle()
            val collectionList by viewModel.currentCollectionList.collectAsStateWithLifecycle()
            val selectedCollection by viewModel.selectedCollection.collectAsStateWithLifecycle()

            RecordsScreen(
                scaffoldAppScreenPadding = scaffoldPadding,
                accountList = appUiState.accountsAndActiveOne.accountList,
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
            val parentCategoryId = backStack.toRoute<MainScreens.CategoryStatistics>().parentCategoryId
            val defaultCollectionName = stringResource(R.string.all_categories)

            val viewModel = viewModel<CategoryStatisticsViewModel>(
                factory = CategoryStatisticsViewModelFactory(
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
                    appUiState.categoryCollectionsUiState.appendDefaultCollection(name = defaultCollectionName)
                )
            }
            LaunchedEffect(
                appUiState.dateRangeMenuUiState.dateRangeWithEnum.enum,
                appUiState.accountsAndActiveOne.accountList
            ) {
                viewModel.clearParentCategoryStatistics()
            }
            LaunchedEffect(true) {
                viewModel.setParentCategoryStatistics()
                viewModel.clearParentCategoryId()
            }

            val parentCategory by viewModel.parentCategoryStatistics.collectAsStateWithLifecycle()
            val categoryStatisticsList by viewModel.categoryStatisticsList.collectAsStateWithLifecycle()
            val categoryType by viewModel.categoryType.collectAsStateWithLifecycle()
            val collectionList by viewModel.currentCollectionList.collectAsStateWithLifecycle()
            val selectedCollection by viewModel.selectedCollection.collectAsStateWithLifecycle()

            CategoryStatisticsScreen(
                scaffoldAppScreenPadding = scaffoldPadding,
                accountList = appUiState.accountsAndActiveOne.accountList,
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
            val viewModel = koinViewModel<BudgetsViewModel>()

            val budgetsByType by viewModel.budgetsByType.collectAsStateWithLifecycle()

            BudgetsScreen(
                screenPadding = scaffoldPadding,
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
            val context = LocalContext.current

            val viewModel = koinViewModel<BudgetStatisticsViewModel> {
                parametersOf(budgetId)
            }

            val budget by viewModel.budget.collectAsStateWithLifecycle()
            val budgetsAccounts by viewModel.accounts.collectAsStateWithLifecycle()
            val budgetTotalAmountsInRanges by viewModel.budgetTotalAmountsInRanges.collectAsStateWithLifecycle()

            val columnChartState by remember(budgetTotalAmountsInRanges) {
                derivedStateOf {
                    budget?.let {
                        ColumnChartUiState.asAmountsByDateRanges(
                            totalAmountsByRanges = budgetTotalAmountsInRanges,
                            rowsCount = 5,
                            repeatingPeriod = it.repeatingPeriod,
                            context = context
                        )
                    } ?: ColumnChartUiState()
                }
            }

            BudgetStatisticsScreen(
                budget = budget,
                columnChartUiState = columnChartState,
                budgetAccounts = budgetsAccounts,
                onBackButtonClick = navController::popBackStack
            )
        }
        composable<MainScreens.RecordCreation>(
            enterTransition = { screenEnterTransition() },
            popEnterTransition = { screenEnterTransition(!moveScreenTowardsLeft) },
            exitTransition = { screenExitTransition(moveScreenTowardsLeft) },
            popExitTransition = { screenExitTransition(false) }
        ) { backStack ->
            val recordNum = backStack.toRoute<MainScreens.RecordCreation>().recordNum

            val viewModel = koinViewModel<RecordCreationViewModel> {
                parametersOf(recordNum)
            }

            val recordDraftGeneral by viewModel.recordDraftGeneral.collectAsStateWithLifecycle()
            val recordDraftItems by viewModel.recordDraftItems.collectAsStateWithLifecycle()
            val savingIsAllowed by viewModel.savingIsAllowed.collectAsStateWithLifecycle()
            val categories by viewModel.groupedCategoriesByType.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            RecordCreationScreen(
                recordDraftGeneral = recordDraftGeneral,
                recordDraftItems = recordDraftItems,
                savingIsAllowed = savingIsAllowed,
                accountList = appUiState.accountsAndActiveOne.accountList,
                groupedCategoriesByType = categories,
                onSelectCategoryType = viewModel::selectCategoryType,
                onNavigateToTransferCreationScreen = {
                    navViewModel.navigateToScreen(
                        navController = navController, screen = MainScreens.TransferCreation()
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
                        viewModel.saveRecord()
                        navController.popBackStack()
                    }
                },
                onRepeatButton = {
                    coroutineScope.launch {
                        viewModel.repeatRecord()
                        navController.popBackStack()
                    }
                },
                onDeleteButton = {
                    coroutineScope.launch {
                        viewModel.deleteRecord()
                        navController.popBackStack()
                    }
                }
            )
        }
        composable<MainScreens.TransferCreation>(
            enterTransition = { screenEnterTransition() },
            popExitTransition = { screenExitTransition(false) }
        ) { backStack ->
            val recordNum = backStack.toRoute<MainScreens.TransferCreation>().recordNum

            val viewModel = koinViewModel<TransferCreationViewModel> {
                parametersOf(recordNum)
            }

            val transferDraft by viewModel.transferDraft.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            TransferCreationScreen(
                transferDraft = transferDraft,
                accountList = appUiState.accountsAndActiveOne.accountList,
                onNavigateBack = navController::popBackStack,
                onSelectNewDate = viewModel::selectNewDate,
                onSelectNewTime = viewModel::selectNewTime,
                onSelectAnotherAccount = viewModel::selectAnotherAccount,
                onSelectAccount = viewModel::selectAccount,
                onRateChange = viewModel::changeRate,
                onAmountChange = viewModel::changeAmount,
                onSaveButton = {
                    coroutineScope.launch {
                        viewModel.saveTransfer()
                        navViewModel.popBackStackToHomeScreen(navController)
                    }
                },
                onRepeatButton = {
                    coroutineScope.launch {
                        viewModel.repeatTransfer()
                        navViewModel.popBackStackToHomeScreen(navController)
                    }
                },
                onDeleteButton = {
                    coroutineScope.launch {
                        viewModel.deleteTransfer()
                        navViewModel.popBackStackToHomeScreen(navController)
                    }
                }
            )
        }
        settingsGraph(
            navController = navController,
            scaffoldPadding = scaffoldPadding,
            navViewModel = navViewModel,
            navigationButtonList = appUiState.navigationButtonList,
            authController = authController,
            appViewModel = appViewModel,
            appConfiguration = appConfiguration,
            themeUiState = themeUiState,
            accountList = appUiState.accountsAndActiveOne.accountList,
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