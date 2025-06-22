package com.ataglance.walletglance.core.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.account.domain.mapper.toRecordAccount
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.presentation.component.AccountWidget
import com.ataglance.walletglance.account.presentation.component.AccountWidgetWrapper
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.presentation.component.container.BudgetsOnWidgetSettingsBottomSheet
import com.ataglance.walletglance.budget.presentation.component.widget.ChosenBudgetsWidget
import com.ataglance.walletglance.budget.presentation.component.widget.ChosenBudgetsWidgetWrapper
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetsOnWidgetSettingsViewModel
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.presentation.component.CategoriesStatisticsWidget
import com.ataglance.walletglance.category.presentation.component.CategoriesStatisticsWidgetWrapper
import com.ataglance.walletglance.category.presentation.model.CategoriesStatisticsByType
import com.ataglance.walletglance.category.presentation.model.CategoriesStatisticsWidgetUiState
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateRangeWithEnum
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.domain.widget.ExpensesIncomeWidgetUiState
import com.ataglance.walletglance.core.presentation.animation.StartAnimatedContainer
import com.ataglance.walletglance.core.presentation.component.container.AppMainTopBar
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.widget.ExpensesIncomeWidget
import com.ataglance.walletglance.core.presentation.component.widget.ExpensesIncomeWidgetWrapper
import com.ataglance.walletglance.core.presentation.component.widget.GreetingsWidget
import com.ataglance.walletglance.core.presentation.component.widget.GreetingsWidgetWrapper
import com.ataglance.walletglance.core.presentation.model.ResourceManagerImpl
import com.ataglance.walletglance.core.presentation.utils.bottom
import com.ataglance.walletglance.core.presentation.utils.plusBottomPadding
import com.ataglance.walletglance.core.presentation.utils.top
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import com.ataglance.walletglance.core.utils.toTimestampRange
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.model.RecordStackItem
import com.ataglance.walletglance.record.domain.model.RecordType
import com.ataglance.walletglance.record.domain.utils.filterByAccount
import com.ataglance.walletglance.record.mapper.toRecordStacks
import com.ataglance.walletglance.record.presentation.component.RecentRecordsWidget
import com.ataglance.walletglance.record.presentation.component.RecentRecordsWidgetWrapper
import com.ataglance.walletglance.record.presentation.model.RecentRecordsWidgetUiState
import com.ataglance.walletglance.recordCreation.presentation.component.RecordCreationWidget
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreenWrapper(
    screenPadding: PaddingValues,
    isAppThemeSetUp: Boolean,
    accountsAndActiveOne: AccountsAndActiveOne,
    onTopBarAccountClick: (Int) -> Unit,
    dateRangeWithEnum: DateRangeWithEnum,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    isCustomDateRangeWindowOpened: Boolean,
    onCustomDateRangeButtonClick: () -> Unit,
    widgetNames: List<WidgetName>,
    onChangeHideActiveAccountBalance: () -> Unit,
    onNavigateToScreenMovingTowardsLeft: (Any) -> Unit
) {
    val budgetsOnWidgetSettingsViewModel = koinViewModel<BudgetsOnWidgetSettingsViewModel>()
    // TODO: fix bug with displaying the bottom sheet

    HomeScreen(
        screenPadding = screenPadding,
        isAppThemeSetUp = isAppThemeSetUp,
        accountsAndActiveOne = accountsAndActiveOne,
        onTopBarAccountClick = onTopBarAccountClick,
        dateRangeWithEnum = dateRangeWithEnum,
        onDateRangeChange = onDateRangeChange,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
        widgetNames = widgetNames,
        onMakeRecord = {
            onNavigateToScreenMovingTowardsLeft(MainScreens.RecordCreation())
        },
        onMakeTransfer = {
            onNavigateToScreenMovingTowardsLeft(MainScreens.TransferCreation())
        },
        greetingsWidget = {
            GreetingsWidgetWrapper()
        },
        accountWidget = {
            AccountWidgetWrapper(
                activeAccount = accountsAndActiveOne.activeAccount,
                onChangeHideActiveAccountBalance = onChangeHideActiveAccountBalance
            )
        },
        chosenBudgetsWidget = {
            ChosenBudgetsWidgetWrapper(
                onSettingsButtonClick = {
                    budgetsOnWidgetSettingsViewModel.openWidgetSettings(WidgetName.ChosenBudgets)
                },
                onNavigateToBudgetsScreen = {
                    onNavigateToScreenMovingTowardsLeft(MainScreens.Budgets)
                },
                onNavigateToBudgetStatisticsScreen = { id ->
                    onNavigateToScreenMovingTowardsLeft(
                        MainScreens.BudgetStatistics(id)
                    )
                }
            )
        },
        expensesIncomeWidget = {
            ExpensesIncomeWidgetWrapper(
                activeAccount = accountsAndActiveOne.activeAccount,
                dateRangeWithEnum = dateRangeWithEnum
            )
        },
        recentRecordsWidget = {
            RecentRecordsWidgetWrapper(
                accountsAndActiveOne = accountsAndActiveOne,
                dateRangeWithEnum = dateRangeWithEnum,
                onRecordClick = { recordNum: Int ->
                    onNavigateToScreenMovingTowardsLeft(
                        MainScreens.RecordCreation(recordNum = recordNum)
                    )
                },
                onTransferClick = { recordNum: Int ->
                    onNavigateToScreenMovingTowardsLeft(
                        MainScreens.TransferCreation(recordNum = recordNum)
                    )
                },
                onNavigateToRecordsScreen = {
                    onNavigateToScreenMovingTowardsLeft(MainScreens.Records)
                }
            )
        },
        categoriesStatisticsWidget = {
            CategoriesStatisticsWidgetWrapper(
                activeAccount = accountsAndActiveOne.activeAccount,
                activeDateRange = dateRangeWithEnum.dateRange,
                onNavigateToCategoriesStatisticsScreen = { categoryId, type ->
                    if (type != null) {
                        onNavigateToScreenMovingTowardsLeft(
                            MainScreens.CategoryStatistics(
                                parentCategoryId = categoryId, type = type.name
                            )
                        )
                    }
                }
            )
        },
        widgetSettingsBottomSheets = {
            BudgetsOnWidgetSettingsBottomSheet(viewModel = budgetsOnWidgetSettingsViewModel)
        }
    )
}

@Composable
fun HomeScreen(
    screenPadding: PaddingValues,
    isAppThemeSetUp: Boolean,
    accountsAndActiveOne: AccountsAndActiveOne,
    onTopBarAccountClick: (Int) -> Unit,
    dateRangeWithEnum: DateRangeWithEnum,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    isCustomDateRangeWindowOpened: Boolean,
    onCustomDateRangeButtonClick: () -> Unit,
    widgetNames: List<WidgetName>,
    onMakeRecord: () -> Unit,
    onMakeTransfer: () -> Unit,
    greetingsWidget: @Composable () -> Unit,
    accountWidget: @Composable () -> Unit,
    chosenBudgetsWidget: @Composable () -> Unit,
    expensesIncomeWidget: @Composable () -> Unit,
    recentRecordsWidget: @Composable () -> Unit,
    categoriesStatisticsWidget: @Composable () -> Unit,
    widgetSettingsBottomSheets: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        topBar = {
            StartAnimatedContainer(visible = isAppThemeSetUp) {
                AppMainTopBar(
                    accountList = accountsAndActiveOne.accounts,
                    currentDateRangeEnum = dateRangeWithEnum.enum,
                    onDateRangeChange = onDateRangeChange,
                    isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
                    onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                    onAccountClick = onTopBarAccountClick,
                    topPadding = screenPadding.calculateTopPadding()
                )
            }
        },
        containerColor = Color.Transparent
    ) { scaffoldHomeScreenPadding ->
        CompactLayout(
            scaffoldPadding = scaffoldHomeScreenPadding.plusBottomPadding(
                padding = screenPadding.calculateBottomPadding()
            ),
            isAppThemeSetUp = isAppThemeSetUp,
            accountsAndActiveOne = accountsAndActiveOne,
            widgetNames = widgetNames,
            onMakeRecord = onMakeRecord,
            onMakeTransfer = onMakeTransfer,
            greetingsWidget = greetingsWidget,
            accountWidget = accountWidget,
            chosenBudgetsWidget = chosenBudgetsWidget,
            expensesIncomeWidget = expensesIncomeWidget,
            recentRecordsWidget = recentRecordsWidget,
            categoriesStatisticsWidget = categoriesStatisticsWidget,
            widgetSettingsBottomSheets = widgetSettingsBottomSheets
        )
    }
}

@Composable
private fun CompactLayout(
    scaffoldPadding: PaddingValues,
    isAppThemeSetUp: Boolean,
    accountsAndActiveOne: AccountsAndActiveOne,
    widgetNames: List<WidgetName>,
    onMakeRecord: () -> Unit,
    onMakeTransfer: () -> Unit,
    greetingsWidget: @Composable () -> Unit,
    accountWidget: @Composable () -> Unit,
    chosenBudgetsWidget: @Composable () -> Unit,
    expensesIncomeWidget: @Composable () -> Unit,
    recentRecordsWidget: @Composable () -> Unit,
    categoriesStatisticsWidget: @Composable () -> Unit,
    widgetSettingsBottomSheets: @Composable BoxScope.() -> Unit
) {
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                top = scaffoldPadding.top + 24.dp,
                bottom = scaffoldPadding.bottom + 24.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                StartAnimatedContainer(
                    visible = isAppThemeSetUp,
                    delayMillis = 50,
                    content = greetingsWidget
                )
            }
            item {
                StartAnimatedContainer(
                    visible = isAppThemeSetUp && accountsAndActiveOne.activeAccount != null,
                    delayMillis = 100,
                    content = accountWidget
                )
            }
            item {
                StartAnimatedContainer(
                    visible = isAppThemeSetUp,
                    delayMillis = 150
                ) {
                    RecordCreationWidget(
                        onMakeRecord = onMakeRecord,
                        onMakeTransfer = onMakeTransfer
                    )
                }
            }
            itemsIndexed(items = widgetNames) { index, widgetName ->
                StartAnimatedContainer(visible = isAppThemeSetUp, delayMillis = (index + 4) * 50) {
                    when (widgetName) {
                        WidgetName.ChosenBudgets -> chosenBudgetsWidget()
                        WidgetName.TotalForPeriod -> expensesIncomeWidget()
                        WidgetName.RecentRecords -> recentRecordsWidget()
                        WidgetName.TopExpenseCategories -> categoriesStatisticsWidget()
                    }
                }
            }
        }
        widgetSettingsBottomSheets()
    }
}



@Preview(
//    heightDp = 2000,
    device = Devices.PIXEL_7_PRO,
    locale = "en"
)
@Composable
fun HomeScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    accountsAndActiveOne: AccountsAndActiveOne = AccountsAndActiveOne(
        accounts = listOf(
            Account(
                id = 1,
                orderNum = 1,
                name = "Main Card CZK",
                currency = "CZK",
                balance = 43551.63,
                color = AccountColors.Pink,
                isActive = true
            ),
            Account(
                id = 2,
                orderNum = 2,
                name = "USD Card",
                currency = "USD",
                balance = 1516.41,
                color = AccountColors.Blue,
                isActive = false
            )
        ),
        activeAccount = Account(
            id = 1,
            orderNum = 1,
            name = "Main Card CZK",
            currency = "CZK",
            balance = 43551.63,
            color = AccountColors.Pink,
            isActive = true
        )
    ),
    dateRangeWithEnum: DateRangeWithEnum = DateRangeWithEnum.fromEnum(
        enum = DateRangeEnum.ThisMonth
    ),
    isCustomDateRangeWindowOpened: Boolean = false,
    widgetNames: List<WidgetName> = listOf(
        WidgetName.ChosenBudgets,
        WidgetName.TopExpenseCategories,
        WidgetName.RecentRecords,
        WidgetName.TotalForPeriod,
    ),
    recordList: List<RecordEntity>? = null,
    recordStackList: List<RecordStack> = recordList?.toRecordStacks(
        accounts = accountsAndActiveOne.accounts,
        groupedCategoriesByType = groupedCategoriesByType
    ) ?: listOf(
        RecordStack(
            recordNum = 1,
            date = getCurrentTimestamp(),
            type = RecordType.Expense,
            account = accountsAndActiveOne.accounts[0].toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackItem(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSub = groupedCategoriesByType
                        .expense[0].getWithFirstSubcategory(),
                    note = null,
                    includeInBudgets = true
                )
            )
        ),
        RecordStack(
            recordNum = 2,
            date = getCurrentTimestamp(),
            type = RecordType.OutTransfer,
            account = accountsAndActiveOne.accounts[0].toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackItem(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSub = groupedCategoriesByType
                        .expense[0].getWithFirstSubcategory(),
                    note = accountsAndActiveOne.accounts[1].id.toString(),
                    includeInBudgets = true
                )
            )
        ),
    ),
    budgetsOnWidget: List<Budget> = listOf(
        Budget(
            id = 1,
            priorityNum = 1.0,
            amountLimit = 4000.0,
            usedAmount = 2250.0,
            usedPercentage = 56.25f,
            category = groupedCategoriesByType.expense[0].category,
            name = groupedCategoriesByType.expense[0].category.name,
            repeatingPeriod = RepeatingPeriod.Monthly,
            dateRange = RepeatingPeriod.Monthly.toTimestampRange(),
            currentTimeWithinRangeGraphPercentage = .5f,
            currency = accountsAndActiveOne.activeAccount?.currency ?: "",
            linkedAccountsIds = listOf(1)
        )
    )
) {
    val context = LocalContext.current
    val resourceManager = ResourceManagerImpl(context = context)

    val categoriesStatisticsWidgetUiState = CategoriesStatisticsByType
        .fromRecordStacks(
            recordStacks = recordStackList.filterByAccount(accountsAndActiveOne.activeAccount!!.id)
        )
        .getExpenseIfNotEmptyOrIncome()
        .let(CategoriesStatisticsWidgetUiState::fromStatistics)

    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isBottomBarVisible = true
    ) { screenPadding ->
        HomeScreen(
            screenPadding = screenPadding,
            isAppThemeSetUp = true,
            accountsAndActiveOne = accountsAndActiveOne,
            onTopBarAccountClick = {},
            dateRangeWithEnum = dateRangeWithEnum,
            onDateRangeChange = {},
            isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
            onCustomDateRangeButtonClick = {},
            widgetNames = widgetNames,
            onMakeRecord = {},
            onMakeTransfer = {},
            greetingsWidget = {
                GreetingsWidget(message = "Good afternoon, Erwin!")
            },
            accountWidget = {
                AccountWidget(
                    account = accountsAndActiveOne.activeAccount,
                    todayExpenses = 50.51,
                    onHideBalanceButton = {}
                )
            },
            chosenBudgetsWidget = {
                ChosenBudgetsWidget(
                    budgets = budgetsOnWidget,
                    resourceManager = resourceManager,
                    onAdjustWidget = {},
                    onNavigateToBudgetsScreen = {},
                    onNavigateToBudgetStatisticsScreen = {}
                )
            },
            expensesIncomeWidget = {
                ExpensesIncomeWidget(
                    uiState = ExpensesIncomeWidgetUiState(
                        expensesTotal = 0.0,
                        incomeTotal = 0.0,
                        expensesPercentage = 0.0,
                        incomePercentage = 0.0,
                        expensesPercentageFloat = 0.0f,
                        incomePercentageFloat = 0.0f
                    ),
                    dateRangeWithEnum = DateRangeWithEnum.fromEnum(enum = DateRangeEnum.ThisMonth),
                    accountCurrency = accountsAndActiveOne.activeAccount.currency,
                    resourceManager = resourceManager
                )
            },
            recentRecordsWidget = {
                RecentRecordsWidget(
                    uiState = RecentRecordsWidgetUiState(
                        firstRecord = recordStackList.getOrNull(0),
                        secondRecord = recordStackList.getOrNull(1),
                        thirdRecord = recordStackList.getOrNull(2)
                    ),
                    accountList = accountsAndActiveOne.accounts,
                    isCustomDateRange = false,
                    resourceManager = resourceManager,
                    onRecordClick = {},
                    onTransferClick = {},
                    onNavigateToRecordsScreen = {}
                )
            },
            categoriesStatisticsWidget = {
                CategoriesStatisticsWidget(
                    uiState = categoriesStatisticsWidgetUiState,
                    onNavigateToCategoriesStatisticsScreen = { _, _ -> }
                )
            },
            widgetSettingsBottomSheets = {}
        )
    }
}
