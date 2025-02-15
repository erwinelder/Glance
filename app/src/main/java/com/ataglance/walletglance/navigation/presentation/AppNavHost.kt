package com.ataglance.walletglance.navigation.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.ataglance.walletglance.categoryCollection.presentation.navigation.CategoryCollectionsSettingsScreens
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppUiState
import com.ataglance.walletglance.core.domain.widgets.WidgetsUiState
import com.ataglance.walletglance.core.presentation.animation.screenEnterTransition
import com.ataglance.walletglance.core.presentation.animation.screenExitTransition
import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.screen.HomeScreen
import com.ataglance.walletglance.core.presentation.screen.SetupFinishScreen
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.record.presentation.screen.RecordsScreen
import com.ataglance.walletglance.record.presentation.viewmodel.RecordsViewModel
import com.ataglance.walletglance.recordCreation.presentation.screen.RecordCreationScreen
import com.ataglance.walletglance.recordCreation.presentation.screen.TransferCreationScreen
import com.ataglance.walletglance.recordCreation.presentation.viewmodel.RecordCreationViewModel
import com.ataglance.walletglance.recordCreation.presentation.viewmodel.TransferCreationViewModel
import com.ataglance.walletglance.settings.navigation.settingsGraph
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
    moveScreenTowardsLeft: Boolean,
    appConfiguration: AppConfiguration,
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

            val viewModel = koinViewModel<RecordsViewModel> {
                parametersOf(
                    appUiState.accountsAndActiveOne.activeAccount,
                    appUiState.dateRangeMenuUiState.dateRangeWithEnum.dateRange,
                    defaultCollectionName
                )
            }

            LaunchedEffect(appUiState.accountsAndActiveOne.activeAccount) {
                viewModel.setActiveAccountId(appUiState.accountsAndActiveOne.activeAccount?.id ?: 0)
            }
            LaunchedEffect(appUiState.dateRangeMenuUiState.dateRangeWithEnum.dateRange) {
                viewModel.setActiveDateRange(appUiState.dateRangeMenuUiState.dateRangeWithEnum.dateRange)
            }

            val collectionsUiState by viewModel.categoryCollectionsUiState.collectAsStateWithLifecycle()
            val recordStacks by viewModel.filteredRecordStacks.collectAsStateWithLifecycle()

            RecordsScreen(
                scaffoldAppScreenPadding = scaffoldPadding,
                accountList = appUiState.accountsAndActiveOne.accountList,
                onAccountClick = appViewModel::applyActiveAccountByOrderNum,
                currentDateRangeEnum = appUiState.dateRangeMenuUiState.dateRangeWithEnum.enum,
                isCustomDateRangeWindowOpened = openCustomDateRangeWindow,
                onDateRangeChange = appViewModel::selectDateRange,
                onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                collectionsUiState = collectionsUiState,
                recordStacks = recordStacks,
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

            val viewModel = koinViewModel<CategoryStatisticsViewModel> {
                parametersOf(
                    parentCategoryId,
                    appUiState.accountsAndActiveOne.activeAccount,
                    appUiState.dateRangeMenuUiState.dateRangeWithEnum.dateRange,
                    defaultCollectionName
                )
            }

            LaunchedEffect(appUiState.accountsAndActiveOne.activeAccount) {
                viewModel.setActiveAccountId(appUiState.accountsAndActiveOne.activeAccount?.id ?: 0)
            }
            LaunchedEffect(appUiState.dateRangeMenuUiState.dateRangeWithEnum.dateRange) {
                viewModel.setActiveDateRange(appUiState.dateRangeMenuUiState.dateRangeWithEnum.dateRange)
            }

            val collectionsUiState by viewModel.categoryCollectionsUiState.collectAsStateWithLifecycle()
            val groupedCategoryStatistics by viewModel.groupedCategoryStatistics.collectAsStateWithLifecycle()

            CategoryStatisticsScreen(
                scaffoldAppScreenPadding = scaffoldPadding,
                accountList = appUiState.accountsAndActiveOne.accountList,
                onAccountClick = appViewModel::applyActiveAccountByOrderNum,
                currentDateRangeEnum = appUiState.dateRangeMenuUiState.dateRangeWithEnum.enum,
                isCustomDateRangeWindowOpened = openCustomDateRangeWindow,
                onDateRangeChange = appViewModel::selectDateRange,
                onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                collectionsUiState = collectionsUiState,
                onToggleCollectionType = viewModel::toggleCollectionType,
                onCollectionSelect = viewModel::selectCollection,

                groupedCategoryStatistics = groupedCategoryStatistics,
                onNavigateToEditCollectionsScreen = {
                    navViewModel.navigateToScreenMovingTowardsLeft(
                        navController = navController,
                        screen = CategoryCollectionsSettingsScreens.EditCategoryCollections
                    )
                },
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

            val viewModel = koinViewModel<BudgetStatisticsViewModel> {
                parametersOf(budgetId)
            }

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            BudgetStatisticsScreen(
                uiState = uiState,
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
            val groupedCategories by viewModel.groupedCategoriesByType.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            RecordCreationScreen(
                recordDraftGeneral = recordDraftGeneral,
                recordDraftItems = recordDraftItems,
                savingIsAllowed = savingIsAllowed,
                accountList = appUiState.accountsAndActiveOne.accountList,
                groupedCategoriesByType = groupedCategories,
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
            authController = authController,
            appViewModel = appViewModel,
            appConfiguration = appConfiguration
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