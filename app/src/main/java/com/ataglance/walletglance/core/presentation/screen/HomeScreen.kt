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
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.ataglance.walletglance.appearanceSettings.domain.model.WidgetName
import com.ataglance.walletglance.category.domain.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.DefaultCategoriesPackage
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState
import com.ataglance.walletglance.core.domain.widgets.GreetingsWidgetUiState
import com.ataglance.walletglance.core.domain.widgets.WidgetsUiState
import com.ataglance.walletglance.core.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.animation.StartAnimatedContainer
import com.ataglance.walletglance.core.presentation.animation.WidgetStartAnimatedContainer
import com.ataglance.walletglance.core.presentation.components.buttons.NavigationTextArrowButton
import com.ataglance.walletglance.core.presentation.components.containers.AppMainTopBar
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.components.widgets.CategoriesStatisticsWidget
import com.ataglance.walletglance.core.presentation.components.widgets.ExpensesIncomeWidget
import com.ataglance.walletglance.core.presentation.components.widgets.GreetingsMessage
import com.ataglance.walletglance.core.presentation.components.widgets.RecordHistoryWidget
import com.ataglance.walletglance.core.utils.getDateRangeMenuUiState
import com.ataglance.walletglance.core.utils.getTodayDateLong
import com.ataglance.walletglance.navigation.utils.isScreen
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.data.mapper.toRecordStackList
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordStackItem
import com.ataglance.walletglance.record.domain.RecordType
import com.ataglance.walletglance.record.utils.getExpensesIncomeWidgetUiState

@Composable
fun HomeScreen(
    scaffoldAppScreenPadding: PaddingValues,
    isAppThemeSetUp: Boolean,
    accountsUiState: AccountsUiState,
    dateRangeMenuUiState: DateRangeMenuUiState,
    widgetNamesList: List<WidgetName>,
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
            StartAnimatedContainer(visible = isAppThemeSetUp) {
                AppMainTopBar(
                    accountList = accountsUiState.accountList,
                    currentDateRangeEnum = dateRangeMenuUiState.dateRangeWithEnum.enum,
                    onDateRangeChange = onDateRangeChange,
                    isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
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
            isAppThemeSetUp = isAppThemeSetUp,
            accountsUiState = accountsUiState,
            dateRangeMenuUiState = dateRangeMenuUiState,
            widgetNamesList = widgetNamesList,
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
    isAppThemeSetUp: Boolean,
    accountsUiState: AccountsUiState,
    dateRangeMenuUiState: DateRangeMenuUiState,
    widgetNamesList: List<WidgetName>,
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
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            StartAnimatedContainer(visible = isAppThemeSetUp, delayMillis = 50) {
                GreetingsMessage(widgetsUiState.greetings.titleRes)
            }
        }
        item {
            StartAnimatedContainer(
                visible = isAppThemeSetUp && accountsUiState.activeAccount != null,
                delayMillis = 100
            ) {
                AccountCard(
                    account = accountsUiState.activeAccount!!,
                    todayExpenses = widgetsUiState.greetings.todayExpensesByActiveAccount,
                    onHideBalanceButton = onChangeHideActiveAccountBalance
                )
            }
        }
        itemsIndexed(items = widgetNamesList) { index, widgetName ->
            when (widgetName) {
                WidgetName.ChosenBudgets -> {
                }
                WidgetName.TotalForPeriod -> {
                    WidgetStartAnimatedContainer(visible = isAppThemeSetUp, index = index) {
                        ExpensesIncomeWidget(
                            uiState = widgetsUiState.expensesIncomeState,
                            dateRangeWithEnum = dateRangeMenuUiState.dateRangeWithEnum,
                            accountCurrency = accountsUiState.activeAccount?.currency ?: ""
                        )
                    }
                }
                WidgetName.RecentRecords -> {
                    WidgetStartAnimatedContainer(visible = isAppThemeSetUp, index = index) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RecordHistoryWidget(
                                recordStackList = widgetsUiState.recordsFilteredByDateAndAccount.take(3),
                                accountList = accountsUiState.accountList,
                                isCustomDateRange =
                                dateRangeMenuUiState.dateRangeWithEnum.enum == DateRangeEnum.Custom,
                                onRecordClick = { recordNum: Int ->
                                    onNavigateToScreenMovingTowardsLeft(
                                        MainScreens.RecordCreation(isNew = false, recordNum = recordNum)
                                    )
                                },
                                onTransferClick = { recordNum: Int ->
                                    onNavigateToScreenMovingTowardsLeft(
                                        MainScreens.TransferCreation(isNew = false, recordNum = recordNum)
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
                WidgetName.TopExpenseCategories -> {
                    WidgetStartAnimatedContainer(visible = isAppThemeSetUp, index = index) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CategoriesStatisticsWidget(
                                categoryStatisticsLists = widgetsUiState.categoryStatisticsLists,
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
    }
}

@Composable
private fun ExpandedLayout(
    scaffoldAppScreenPadding: PaddingValues,
    scaffoldHomeScreenPadding: PaddingValues,
    isAppThemeSetUp: Boolean,
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
                StartAnimatedContainer(isAppThemeSetUp, 50) {
                    GreetingsMessage(widgetsUiState.greetings.titleRes)
                }
                StartAnimatedContainer(
                    isAppThemeSetUp && accountsUiState.activeAccount != null,
                    delayMillis = 100
                ) {
                    AccountCard(
                        account = accountsUiState.activeAccount!!,
                        todayExpenses = widgetsUiState.greetings.todayExpensesByActiveAccount,
                        onHideBalanceButton = onChangeHideActiveAccountBalance
                    )
                }
            }
            StartAnimatedContainer(isAppThemeSetUp, 150) {
                ExpensesIncomeWidget(
                    uiState = widgetsUiState.expensesIncomeState,
                    dateRangeWithEnum = dateRangeMenuUiState.dateRangeWithEnum,
                    accountCurrency = accountsUiState.activeAccount?.currency ?: ""
                )
            }
        }

        StartAnimatedContainer(isAppThemeSetUp, 200) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                RecordHistoryWidget(
                    recordStackList = filteredRecordStackList.take(3),
                    accountList = accountsUiState.accountList,
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

        StartAnimatedContainer(isAppThemeSetUp, 250) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                CategoriesStatisticsWidget(
                    categoryStatisticsLists = widgetsUiState.categoryStatisticsLists,
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
    apiLevel = 34,
    heightDp = 1900,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun HomeScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    isSetupProgressTopBarVisible: Boolean = false,
    isBottomBarVisible: Boolean = true,
    categoriesWithSubcategories: CategoriesWithSubcategories = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    accountsUiState: AccountsUiState = AccountsUiState(
        accountList = listOf(
            Account(id = 1, orderNum = 1, isActive = true),
            Account(id = 2, orderNum = 2, isActive = false)
        ),
        activeAccount = Account(id = 1, orderNum = 1, isActive = true)
    ),
    dateRangeMenuUiState: DateRangeMenuUiState = DateRangeEnum.ThisMonth.getDateRangeMenuUiState(),
    isCustomDateRangeWindowOpened: Boolean = false,
    recordList: List<RecordEntity>? = null,
    recordStackList: List<RecordStack> = recordList?.toRecordStackList(
        accountList = accountsUiState.accountList,
        categoriesWithSubcategories = categoriesWithSubcategories
    ) ?: listOf(
        RecordStack(
            recordNum = 1,
            date = getTodayDateLong(),
            type = RecordType.Expense,
            account = Account().toRecordAccount(),
            totalAmount = 42.43,
            stack = listOf(
                RecordStackItem(
                    id = 1,
                    amount = 46.47,
                    quantity = null,
                    categoryWithSubcategory = categoriesWithSubcategories
                        .expense[0].getWithFirstSubcategory(),
                    note = null,
                    includeInBudgets = true
                )
            )
        )
    )
) {
    val widgetUiState = WidgetsUiState(
        recordsFilteredByDateAndAccount = recordStackList,
        greetings = GreetingsWidgetUiState(
            titleRes = R.string.greetings_title_afternoon,
            todayExpensesByActiveAccount = 0.0
        ),
        expensesIncomeState = recordStackList.getExpensesIncomeWidgetUiState(),
        categoryStatisticsLists = categoriesWithSubcategories.getStatistics(recordStackList)
    )

    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        anyScreenInHierarchyIsScreenProvider = { it.isScreen(MainScreens.Home) }
    ) { scaffoldPadding ->
        HomeScreen(
            scaffoldAppScreenPadding = scaffoldPadding,
            isAppThemeSetUp = true,
            accountsUiState = accountsUiState,
            dateRangeMenuUiState = dateRangeMenuUiState,
            widgetNamesList = listOf(
                WidgetName.ChosenBudgets,
                WidgetName.TopExpenseCategories,
                WidgetName.RecentRecords,
                WidgetName.TotalForPeriod,
            ),
            widgetsUiState = widgetUiState,
            onChangeHideActiveAccountBalance = {},
            onDateRangeChange = {},
            isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
            onCustomDateRangeButtonClick = {},
            onTopBarAccountClick = {},
            onNavigateToScreenMovingTowardsLeft = {}
        )
    }
}
