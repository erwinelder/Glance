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
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.presentation.component.CategoriesStatisticsWidget
import com.ataglance.walletglance.category.presentation.component.CategoriesStatisticsWidgetWrapper
import com.ataglance.walletglance.category.presentation.model.CategoriesStatistics
import com.ataglance.walletglance.category.presentation.model.CategoriesStatisticsWidgetUiState
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateRangeWithEnum
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.domain.widget.ExpensesIncomeWidgetUiState
import com.ataglance.walletglance.core.presentation.animation.StartAnimatedContainer
import com.ataglance.walletglance.core.presentation.component.container.AppMainTopBar
import com.ataglance.walletglance.core.presentation.component.widget.ExpensesIncomeWidget
import com.ataglance.walletglance.core.presentation.component.widget.ExpensesIncomeWidgetWrapper
import com.ataglance.walletglance.core.presentation.component.widget.GreetingsWidget
import com.ataglance.walletglance.core.presentation.component.widget.GreetingsWidgetWrapper
import com.ataglance.walletglance.core.presentation.model.ResourceManagerImpl
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.utils.bottom
import com.ataglance.walletglance.core.presentation.utils.plusBottomPadding
import com.ataglance.walletglance.core.presentation.utils.top
import com.ataglance.walletglance.core.utils.toTimestamp
import com.ataglance.walletglance.core.utils.toTimestampRange
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.transaction.domain.model.Record
import com.ataglance.walletglance.transaction.domain.model.RecordItem
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems
import com.ataglance.walletglance.transaction.domain.model.Transaction
import com.ataglance.walletglance.transaction.domain.model.Transfer
import com.ataglance.walletglance.transaction.domain.model.TransferItem
import com.ataglance.walletglance.transaction.mapper.toUiStates
import com.ataglance.walletglance.transaction.presentation.component.RecentTransactionsWidget
import com.ataglance.walletglance.transaction.presentation.component.RecentTransactionsWidgetWrapper
import com.ataglance.walletglance.transaction.presentation.component.TransactionCreationWidget
import com.ataglance.walletglance.transaction.presentation.model.RecentTransactionsWidgetUiState
import kotlinx.datetime.LocalDateTime
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
            RecentTransactionsWidgetWrapper(
                accountsAndActiveOne = accountsAndActiveOne,
                dateRangeWithEnum = dateRangeWithEnum,
                onRecordClick = { id ->
                    onNavigateToScreenMovingTowardsLeft(
                        MainScreens.RecordCreation(recordId = id)
                    )
                },
                onTransferClick = { id ->
                    onNavigateToScreenMovingTowardsLeft(
                        MainScreens.TransferCreation(transferId = id)
                    )
                },
                onNavigateToRecordsScreen = {
                    onNavigateToScreenMovingTowardsLeft(MainScreens.Transactions)
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
                    TransactionCreationWidget(
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
    categoryType: CategoryType = CategoryType.Expense,
    transactions: List<Transaction> = listOf(
        RecordWithItems(
            record = Record(
                id = 1,
                date = LocalDateTime(2024, 9, 24, 12, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accountsAndActiveOne.activeAccount!!.id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 1,
                    recordId = 1,
                    totalAmount = 68.43,
                    quantity = null,
                    categoryId = 1,
                    subcategoryId = 13,
                    note = "bread, milk"
                ),
                RecordItem(
                    id = 2,
                    recordId = 1,
                    totalAmount = 178.9,
                    quantity = null,
                    categoryId = 3,
                    subcategoryId = 24,
                    note = "shampoo"
                )
            )
        ),
        Transfer(
            id = 1,
            date = LocalDateTime(2024, 9, 23, 0, 0).toTimestamp(),
            sender = TransferItem(
                accountId = accountsAndActiveOne.activeAccount.id,
                amount = 3000.0,
                rate = 1.0
            ),
            receiver = TransferItem(
                accountId = accountsAndActiveOne.accounts[1].id,
                amount = 3000.0,
                rate = 1.0
            ),
            includeInBudgets = true
        ),
        RecordWithItems(
            record = Record(
                id = 4,
                date = LocalDateTime(2024, 9, 18, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accountsAndActiveOne.activeAccount.id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 4,
                    recordId = 4,
                    totalAmount = 120.9,
                    quantity = null,
                    categoryId = 6,
                    subcategoryId = 40,
                    note = "Music platform"
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 5,
                date = LocalDateTime(2024, 9, 15, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accountsAndActiveOne.activeAccount.id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 5,
                    recordId = 5,
                    totalAmount = 799.9,
                    quantity = null,
                    categoryId = 3,
                    subcategoryId = 21,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 6,
                date = LocalDateTime(2024, 9, 12, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accountsAndActiveOne.activeAccount.id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 6,
                    recordId = 6,
                    totalAmount = 3599.9,
                    quantity = null,
                    categoryId = 1,
                    subcategoryId = 13,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 7,
                date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accountsAndActiveOne.activeAccount.id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 7,
                    recordId = 7,
                    totalAmount = 8500.0,
                    quantity = null,
                    categoryId = 2,
                    subcategoryId = 15,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 8,
                date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
                type = CategoryType.Income,
                accountId = accountsAndActiveOne.activeAccount.id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 8,
                    recordId = 8,
                    totalAmount = 42600.0,
                    quantity = null,
                    categoryId = 72,
                    subcategoryId = null,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 9,
                date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accountsAndActiveOne.activeAccount.id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 9,
                    recordId = 9,
                    totalAmount = 799.9,
                    quantity = null,
                    categoryId = 6,
                    subcategoryId = 38,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 10,
                date = LocalDateTime(2024, 6, 4, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accountsAndActiveOne.accounts[1].id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 10,
                    recordId = 10,
                    totalAmount = 450.41,
                    quantity = null,
                    categoryId = 9,
                    subcategoryId = 50,
                    note = null
                )
            )
        ),
        RecordWithItems(
            record = Record(
                id = 10,
                date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
                type = CategoryType.Expense,
                accountId = accountsAndActiveOne.activeAccount.id,
                includeInBudgets = true
            ),
            items = listOf(
                RecordItem(
                    id = 10,
                    recordId = 10,
                    totalAmount = 690.56,
                    quantity = null,
                    categoryId = 10,
                    subcategoryId = 58,
                    note = null
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
            linkedAccountIds = listOf(1)
        )
    )
) {
    val context = LocalContext.current
    val resourceManager = ResourceManagerImpl(context = context)

    val categoriesStatisticsWidgetUiState = CategoriesStatistics
        .fromTransactions(
            type = categoryType,
            accountId = accountsAndActiveOne.activeAccount!!.id,
            accountCurrency = accountsAndActiveOne.activeAccount.currency,
            transactions = transactions,
            groupedCategories = groupedCategoriesByType.getByType(type = categoryType)
        )
        .let { CategoriesStatisticsWidgetUiState.fromStatistics(it.stats) }

    val transactions = transactions.toUiStates(
        accountId = accountsAndActiveOne.activeAccount.id,
        accounts = accountsAndActiveOne.accounts,
        groupedCategoriesByType = groupedCategoriesByType,
        resourceManager = resourceManager
    )

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
                RecentTransactionsWidget(
                    uiState = RecentTransactionsWidgetUiState(
                        firstRecord = transactions.getOrNull(0),
                        secondRecord = transactions.getOrNull(1),
                        thirdRecord = transactions.getOrNull(2)
                    ),
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
