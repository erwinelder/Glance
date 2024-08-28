package com.ataglance.walletglance.core.presentation.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.AccountsUiState
import com.ataglance.walletglance.account.presentation.components.AccountCard
import com.ataglance.walletglance.category.domain.CategoryStatisticsLists
import com.ataglance.walletglance.category.domain.DefaultCategoriesPackage
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState
import com.ataglance.walletglance.core.domain.widgets.ExpensesIncomeWidgetUiState
import com.ataglance.walletglance.core.domain.widgets.GreetingsWidgetUiState
import com.ataglance.walletglance.core.domain.widgets.WidgetsUiState
import com.ataglance.walletglance.navigation.domain.model.MainScreens
import com.ataglance.walletglance.core.presentation.animation.StartAnimatedContainer
import com.ataglance.walletglance.core.presentation.components.buttons.NavigationTextArrowButton
import com.ataglance.walletglance.core.presentation.components.containers.AppMainTopBar
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.components.widgets.CategoriesStatisticsWidget
import com.ataglance.walletglance.core.presentation.components.widgets.ExpensesIncomeWidget
import com.ataglance.walletglance.core.presentation.components.widgets.GreetingsMessage
import com.ataglance.walletglance.core.presentation.components.widgets.RecordHistoryWidget
import com.ataglance.walletglance.core.utils.getDateRangeMenuUiState
import com.ataglance.walletglance.core.utils.getTodayDateLong
import com.ataglance.walletglance.core.utils.isScreen
import com.ataglance.walletglance.makingRecord.domain.MakeRecordStatus
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordStackUnit
import com.ataglance.walletglance.record.domain.RecordType

@Composable
fun HomeScreen(
    scaffoldAppScreenPadding: PaddingValues,
    appTheme: AppTheme?,
    accountsUiState: AccountsUiState,
    dateRangeMenuUiState: DateRangeMenuUiState,
    widgetsUiState: WidgetsUiState,
    onChangeHideActiveAccountBalance: () -> Unit,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    isCustomDateRangeWindowOpened: Boolean,
    onCustomDateRangeButtonClick: () -> Unit,
    onTopBarAccountClick: (Int) -> Unit,
    onNavigateToScreenMovingTowardsLeft: (Any) -> Unit
) {
    Scaffold(
        topBar = {
            StartAnimatedContainer(appTheme != null) {
                AppMainTopBar(
                    accountList = accountsUiState.accountList,
                    currentDateRangeEnum = dateRangeMenuUiState.dateRangeWithEnum.enum,
                    onDateRangeChange = onDateRangeChange,
                    isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
                    appTheme = appTheme,
                    onCustomDateRangeButtonClick = onCustomDateRangeButtonClick,
                    onAccountClick = onTopBarAccountClick
                )
            }
        },
        containerColor = Color.Transparent
    ) { scaffoldHomeScreenPadding ->
        CompactLayout(
            scaffoldAppScreenPadding = scaffoldAppScreenPadding,
            scaffoldHomeScreenPadding = scaffoldHomeScreenPadding,
            appTheme = appTheme,
            accountsUiState = accountsUiState,
            dateRangeMenuUiState = dateRangeMenuUiState,
            widgetsUiState = widgetsUiState,
            onChangeHideActiveAccountBalance = onChangeHideActiveAccountBalance,
            onNavigateToScreenMovingTowardsLeft = onNavigateToScreenMovingTowardsLeft
        )
    }
}

@Composable
private fun CompactLayout(
    scaffoldAppScreenPadding: PaddingValues,
    scaffoldHomeScreenPadding: PaddingValues,
    appTheme: AppTheme?,
    accountsUiState: AccountsUiState,
    dateRangeMenuUiState: DateRangeMenuUiState,
    widgetsUiState: WidgetsUiState,
    onChangeHideActiveAccountBalance: () -> Unit,
    onNavigateToScreenMovingTowardsLeft: (Any) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(
            top = scaffoldAppScreenPadding.calculateTopPadding() +
                    scaffoldHomeScreenPadding.calculateTopPadding() +
                    dimensionResource(R.dimen.screen_vertical_padding),
            bottom = scaffoldAppScreenPadding.calculateBottomPadding() +
                    scaffoldHomeScreenPadding.calculateBottomPadding() +
                    dimensionResource(R.dimen.screen_vertical_padding)
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.widgets_gap)),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            StartAnimatedContainer(visible = appTheme != null, delayMillis = 50) {
                GreetingsMessage(widgetsUiState.greetings.titleRes)
            }
        }
        item {
            StartAnimatedContainer(
                visible = appTheme != null && accountsUiState.activeAccount != null,
                delayMillis = 100
            ) {
                AccountCard(
                    account = accountsUiState.activeAccount!!,
                    appTheme = appTheme,
                    todayExpenses = widgetsUiState.greetings.expensesTotal,
                    onHideBalanceButton = onChangeHideActiveAccountBalance
                )
            }
        }
        item {
            StartAnimatedContainer(visible = appTheme != null, delayMillis = 150) {
                ExpensesIncomeWidget(
                    uiState = widgetsUiState.expensesIncomeState,
                    dateRangeWithEnum = dateRangeMenuUiState.dateRangeWithEnum,
                    accountCurrency = accountsUiState.activeAccount?.currency ?: ""
                )
            }
        }
        item {
            StartAnimatedContainer(visible = appTheme != null, delayMillis = 200) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RecordHistoryWidget(
                        recordStackList = widgetsUiState.recordsFilteredByDateAndAccount.take(3),
                        accountList = accountsUiState.accountList,
                        appTheme = appTheme,
                        isCustomDateRange =
                            dateRangeMenuUiState.dateRangeWithEnum.enum == DateRangeEnum.Custom,
                        onRecordClick = { recordNum: Int ->
                            onNavigateToScreenMovingTowardsLeft(
                                MainScreens.MakeRecord(MakeRecordStatus.Edit.name, recordNum)
                            )
                        },
                        onTransferClick = { recordNum: Int ->
                            onNavigateToScreenMovingTowardsLeft(
                                MainScreens.MakeTransfer(MakeRecordStatus.Edit.name, recordNum)
                            )
                        }
                    )
                    NavigationTextArrowButton(
                        text = stringResource(R.string.view_all),
                        onClick = {
                            onNavigateToScreenMovingTowardsLeft(MainScreens.Records)
                        }
                    )
                }
            }
        }
        item {
            StartAnimatedContainer(visible = appTheme != null, delayMillis = 250) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CategoriesStatisticsWidget(
                        categoryStatisticsLists = widgetsUiState.categoryStatisticsLists,
                        appTheme = appTheme,
                        onNavigateToCategoriesStatisticsScreen = { parentCategoryId ->
                            onNavigateToScreenMovingTowardsLeft(
                                MainScreens.CategoryStatistics(parentCategoryId)
                            )
                        }
                    )
                    NavigationTextArrowButton(text = stringResource(R.string.view_all)) {
                        onNavigateToScreenMovingTowardsLeft(MainScreens.CategoryStatistics(0))
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpandedLayout(
    scaffoldAppScreenPadding: PaddingValues,
    scaffoldHomeScreenPadding: PaddingValues,
    appTheme: AppTheme?,
    accountsUiState: AccountsUiState,
    dateRangeMenuUiState: DateRangeMenuUiState,
    filteredRecordStackList: List<RecordStack>,
    widgetsUiState: WidgetsUiState,
    onChangeHideActiveAccountBalance: () -> Unit,
    onNavigateToRecordsScreen: () -> Unit,
    onRecordClick: (Int) -> Unit,
    onNavigateToCategoriesStatisticsScreen: (Int) -> Unit,
    onTransferClick: (Int) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(
                top = scaffoldAppScreenPadding.calculateTopPadding() +
                        scaffoldHomeScreenPadding.calculateTopPadding() +
                        dimensionResource(R.dimen.screen_vertical_padding),
                bottom = scaffoldAppScreenPadding.calculateBottomPadding() +
                        scaffoldHomeScreenPadding.calculateBottomPadding() +
                        dimensionResource(R.dimen.screen_vertical_padding)
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Red)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(.42f)
            ) {
                StartAnimatedContainer(appTheme != null, 50) {
                    GreetingsMessage(widgetsUiState.greetings.titleRes)
                }
                StartAnimatedContainer(
                    appTheme != null && accountsUiState.activeAccount != null,
                    delayMillis = 100
                ) {
                    AccountCard(
                        account = accountsUiState.activeAccount!!,
                        appTheme = appTheme,
                        todayExpenses = widgetsUiState.greetings.expensesTotal,
                        onHideBalanceButton = onChangeHideActiveAccountBalance
                    )
                }
            }
            StartAnimatedContainer(appTheme != null, 150) {
                ExpensesIncomeWidget(
                    uiState = widgetsUiState.expensesIncomeState,
                    dateRangeWithEnum = dateRangeMenuUiState.dateRangeWithEnum,
                    accountCurrency = accountsUiState.activeAccount?.currency ?: ""
                )
            }
        }

        StartAnimatedContainer(appTheme != null, 200) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                RecordHistoryWidget(
                    recordStackList = filteredRecordStackList.take(3),
                    accountList = accountsUiState.accountList,
                    appTheme = appTheme,
                    isCustomDateRange =
                        dateRangeMenuUiState.dateRangeWithEnum.enum == DateRangeEnum.Custom,
                    onRecordClick = onRecordClick,
                    onTransferClick = onTransferClick
                )
                NavigationTextArrowButton(
                    text = stringResource(R.string.view_all),
                    onClick = onNavigateToRecordsScreen
                )
            }
        }

        StartAnimatedContainer(appTheme != null, 250) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                CategoriesStatisticsWidget(
                    categoryStatisticsLists = widgetsUiState.categoryStatisticsLists,
                    appTheme = appTheme,
                    onNavigateToCategoriesStatisticsScreen =
                        onNavigateToCategoriesStatisticsScreen
                )
                NavigationTextArrowButton(
                    text = stringResource(R.string.view_all),
                    onClick = { onNavigateToCategoriesStatisticsScreen(0) }
                )
            }
        }

    }
}



@Preview(
    name = "HomeScreen",
    group = "MainScreens",
    apiLevel = 34,
    heightDp = 1900,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun HomeScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetup: Boolean = true,
    isSetupProgressTopBarVisible: Boolean = false,
    isBottomBarVisible: Boolean = true,
    accountsUiState: AccountsUiState = AccountsUiState(
        accountList = listOf(
            Account(id = 1, orderNum = 1, isActive = true),
            Account(id = 2, orderNum = 2, isActive = false)
        ),
        activeAccount = Account(id = 1, orderNum = 1, isActive = true)
    ),
    dateRangeMenuUiState: DateRangeMenuUiState = DateRangeEnum.ThisMonth.getDateRangeMenuUiState(),
    widgetsUiState: WidgetsUiState = WidgetsUiState(
        recordsFilteredByDateAndAccount = listOf(
            RecordStack(
                recordNum = 1,
                date = getTodayDateLong(),
                type = RecordType.Expense,
                account = Account().toRecordAccount(),
                totalAmount = 42.43,
                stack = listOf(
                    RecordStackUnit(
                        id = 1,
                        amount = 46.47,
                        quantity = null,
                        categoryWithSubcategory = DefaultCategoriesPackage(LocalContext.current)
                            .getDefaultCategories().expense[0].getWithFirstSubcategory(),
                        note = null,
                        includeInBudgets = true
                    )
                )
            )
        ),
        greetings = GreetingsWidgetUiState(),
        expensesIncomeState = ExpensesIncomeWidgetUiState(
            expensesTotal = 26.27,
            incomeTotal = 28.29,
            expensesPercentage = 30.31,
            incomePercentage = 32.33,
            expensesPercentageFloat = .25f,
            incomePercentageFloat = .75f
        ),
        categoryStatisticsLists = CategoryStatisticsLists()
    ),
    isCustomDateRangeWindowOpened: Boolean = false
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        anyScreenInHierarchyIsScreenProvider = { it.isScreen(MainScreens.Home) }
    ) { scaffoldPadding ->
        HomeScreen(
            scaffoldAppScreenPadding = scaffoldPadding,
            appTheme = appTheme,
            accountsUiState = accountsUiState,
            dateRangeMenuUiState = dateRangeMenuUiState,
            widgetsUiState = widgetsUiState,
            onChangeHideActiveAccountBalance = {},
            onDateRangeChange = {},
            isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
            onCustomDateRangeButtonClick = {},
            onTopBarAccountClick = {},
            onNavigateToScreenMovingTowardsLeft = {}
        )
    }
}
