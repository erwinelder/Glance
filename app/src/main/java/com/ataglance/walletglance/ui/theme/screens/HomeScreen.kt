package com.ataglance.walletglance.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.model.AccountController
import com.ataglance.walletglance.model.AccountsUiState
import com.ataglance.walletglance.model.CategoriesUiState
import com.ataglance.walletglance.model.CategoryController
import com.ataglance.walletglance.model.DateRangeEnum
import com.ataglance.walletglance.model.DateRangeMenuUiState
import com.ataglance.walletglance.model.RecordStack
import com.ataglance.walletglance.model.RecordType
import com.ataglance.walletglance.model.WidgetsUiState
import com.ataglance.walletglance.ui.theme.animation.CustomAnimation
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.AppMainTopBar
import com.ataglance.walletglance.ui.theme.uielements.accounts.AccountCard
import com.ataglance.walletglance.ui.theme.uielements.buttons.NavigationTextArrowButton
import com.ataglance.walletglance.ui.theme.widgets.CategoriesStatisticsWidget
import com.ataglance.walletglance.ui.theme.widgets.ExpensesIncomeWidget
import com.ataglance.walletglance.ui.theme.widgets.GreetingsMessage
import com.ataglance.walletglance.ui.theme.widgets.RecordHistory

@Composable
fun HomeScreen(
    scaffoldAppScreenPadding: PaddingValues,
    appTheme: AppTheme?,
    accountsUiState: AccountsUiState,
    dateRangeMenuUiState: DateRangeMenuUiState,
    filteredRecordStackList: List<RecordStack>,
    categoriesUiState: CategoriesUiState,
    categoryNameAndIconMap: Map<String, Int>,
    widgetsUiState: WidgetsUiState,
    onChangeHideActiveAccountBalance: () -> Unit,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    isCustomDateRangeWindowOpened: Boolean,
    onCustomDateRangeButtonClick: () -> Unit,
    onTopBarAccountClick: (Int) -> Unit,
    onNavigateToRecordsScreen: () -> Unit,
    onNavigateToCategoriesStatisticsScreen: (Int) -> Unit,
    onRecordClick: (Int) -> Unit,
    onTransferClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            WidgetAnimatedContainer(appTheme != null) {
                AppMainTopBar(
                    accountList = accountsUiState.accountList.filter { !it.hide },
                    currentDateRangeEnum = dateRangeMenuUiState.dateRangeState.enum,
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
            filteredRecordStackList = filteredRecordStackList,
            categoriesUiState = categoriesUiState,
            categoryNameAndIconMap = categoryNameAndIconMap,
            widgetsUiState = widgetsUiState,
            onChangeHideActiveAccountBalance = onChangeHideActiveAccountBalance,
            onNavigateToRecordsScreen = onNavigateToRecordsScreen,
            onNavigateToCategoriesStatisticsScreen = onNavigateToCategoriesStatisticsScreen,
            onRecordClick = onRecordClick,
            onTransferClick = onTransferClick
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
    filteredRecordStackList: List<RecordStack>,
    categoriesUiState: CategoriesUiState,
    categoryNameAndIconMap: Map<String, Int>,
    widgetsUiState: WidgetsUiState,
    onChangeHideActiveAccountBalance: () -> Unit,
    onNavigateToRecordsScreen: () -> Unit,
    onRecordClick: (Int) -> Unit,
    onNavigateToCategoriesStatisticsScreen: (Int) -> Unit,
    onTransferClick: (Int) -> Unit
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
            WidgetAnimatedContainer(appTheme != null, 50) {
                GreetingsMessage(widgetsUiState.greetings.titleRes)
            }
        }
        item {
            WidgetAnimatedContainer(
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
        item {
            WidgetAnimatedContainer(appTheme != null, 150) {
                ExpensesIncomeWidget(
                    uiState = widgetsUiState.expensesIncome,
                    dateRangeState = dateRangeMenuUiState.dateRangeState,
                    accountCurrency = accountsUiState.activeAccount?.currency ?: ""
                )
            }
        }
        item {
            WidgetAnimatedContainer(appTheme != null, 200) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RecordHistory(
                        recordStackList = filteredRecordStackList.take(3),
                        appTheme = appTheme,
                        getCategoryAndIcon = { categoryId: Int, subcategoryId: Int?, type: RecordType? ->
                            CategoryController().getCategoryAndIconRes(
                                categoriesUiState = categoriesUiState,
                                categoryNameAndIconMap = categoryNameAndIconMap,
                                categoryId = categoryId,
                                subcategoryId = subcategoryId,
                                recordType = type
                            )
                        },
                        getAccount = { accountId: Int ->
                            AccountController().getAccountById(accountId, accountsUiState.accountList)
                        },
                        onRecordClick = onRecordClick,
                        onTransferClick = onTransferClick,
                        modifier = Modifier.height(370.dp),
                        title = stringResource(R.string.recent)
                    )
                    NavigationTextArrowButton(
                        text = stringResource(R.string.view_all),
                        onClick = onNavigateToRecordsScreen
                    )
                }
            }
        }
        item {
            WidgetAnimatedContainer(appTheme != null, 250) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CategoriesStatisticsWidget(
                        uiState = widgetsUiState.categoryStatisticsLists,
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
}

@Composable
private fun ExpandedLayout(
    scaffoldAppScreenPadding: PaddingValues,
    scaffoldHomeScreenPadding: PaddingValues,
    appTheme: AppTheme?,
    accountsUiState: AccountsUiState,
    dateRangeMenuUiState: DateRangeMenuUiState,
    filteredRecordStackList: List<RecordStack>,
    categoriesUiState: CategoriesUiState,
    categoryNameAndIconMap: Map<String, Int>,
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
                WidgetAnimatedContainer(appTheme != null, 50) {
                    GreetingsMessage(widgetsUiState.greetings.titleRes)
                }
                WidgetAnimatedContainer(
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
            WidgetAnimatedContainer(appTheme != null, 150) {
                ExpensesIncomeWidget(
                    uiState = widgetsUiState.expensesIncome,
                    dateRangeState = dateRangeMenuUiState.dateRangeState,
                    accountCurrency = accountsUiState.activeAccount?.currency ?: ""
                )
            }
        }

        WidgetAnimatedContainer(appTheme != null, 200) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                RecordHistory(
                    recordStackList = filteredRecordStackList.take(3),
                    appTheme = appTheme,
                    getCategoryAndIcon = { categoryId: Int, subcategoryId: Int?, type: RecordType? ->
                        CategoryController().getCategoryAndIconRes(
                            categoriesUiState = categoriesUiState,
                            categoryNameAndIconMap = categoryNameAndIconMap,
                            categoryId = categoryId,
                            subcategoryId = subcategoryId,
                            recordType = type
                        )
                    },
                    getAccount = { accountId: Int ->
                        AccountController().getAccountById(accountId, accountsUiState.accountList)
                    },
                    onRecordClick = onRecordClick,
                    onTransferClick = onTransferClick,
                    modifier = Modifier.height(370.dp),
                    title = stringResource(R.string.recent)
                )
                NavigationTextArrowButton(
                    text = stringResource(R.string.view_all),
                    onClick = onNavigateToRecordsScreen
                )
            }
        }

        WidgetAnimatedContainer(appTheme != null, 250) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                CategoriesStatisticsWidget(
                    uiState = widgetsUiState.categoryStatisticsLists,
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

@Composable
fun WidgetAnimatedContainer(
    visible: Boolean,
    delayMillis: Int = 0,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = CustomAnimation().widgetEnterTransition(delayMillis),
    ) {
        content()
    }
}
