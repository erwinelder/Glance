package com.ataglance.walletglance.presentation.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.accounts.color.AccountPossibleColors
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.budgets.TotalAmountByRange
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.date.RepeatingPeriod
import com.ataglance.walletglance.data.statistics.ColumnChartUiState
import com.ataglance.walletglance.data.utils.formatWithSpaces
import com.ataglance.walletglance.data.utils.getLongDateRangeWithTime
import com.ataglance.walletglance.data.utils.getPrevDateRanges
import com.ataglance.walletglance.data.utils.getSpendingInRecentStringRes
import com.ataglance.walletglance.data.utils.toAccountColorWithName
import com.ataglance.walletglance.presentation.theme.GlanceTheme
import com.ataglance.walletglance.presentation.theme.uielements.accounts.AccountsFlowRow
import com.ataglance.walletglance.presentation.theme.uielements.categories.CategoryBigIconComponent
import com.ataglance.walletglance.presentation.theme.uielements.charts.GlanceColumnChart
import com.ataglance.walletglance.presentation.theme.uielements.charts.GlanceSingleValuePieChart
import com.ataglance.walletglance.presentation.theme.uielements.containers.BackButtonBlock
import com.ataglance.walletglance.presentation.theme.uielements.containers.PreviewContainer

@Composable
fun BudgetStatisticsScreen(
    appTheme: AppTheme?,
    budget: Budget,
    columnChartUiState: ColumnChartUiState,
    budgetAccounts: List<Account>,
    onBackButtonClick: () -> Unit
) {
    val nestedScrollInterop = rememberNestedScrollInteropConnection()
    val averageSpending by remember {
        derivedStateOf { columnChartUiState.averageValue.formatWithSpaces(budget.currency) }
    }
    val verticalGap = 16.dp

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
    ) {
        BackButtonBlock(onBackButtonClick)
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            contentPadding = PaddingValues(bottom = 8.dp),
            modifier = Modifier
                .nestedScroll(nestedScrollInterop)
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.screen_horizontal_padding))
        ) {
            item { Spacer(modifier = Modifier.height(verticalGap)) }
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    budget.category?.let {
                        CategoryBigIconComponent(category = it, appTheme = appTheme)
                    }
                    Text(
                        text = budget.name,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(
                            R.string.amount_currency_spending_limit,
                            budget.amountLimit.formatWithSpaces(), budget.currency
                        ),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    AccountsFlowRow(accountList = budgetAccounts, appTheme = appTheme, maxLines = 5)
                }
            }
            item { Spacer(modifier = Modifier.height(verticalGap)) }
            item {
                GlanceColumnChart(
                    uiState = columnChartUiState,
                    columnsColor = budget.category?.getColorByTheme(appTheme)?.lighter,
                    title = stringResource(
                        id = budget.repeatingPeriod.getSpendingInRecentStringRes(), budget.currency
                    ),
                    bottomNote = "Average spending: $averageSpending"
                ) { totalAmountByPeriod ->
                    StatisticByPeriodDetailsPopupContent(budget, totalAmountByPeriod)
                }
            }
            item { Spacer(modifier = Modifier.height(verticalGap)) }
        }
    }
}

@Composable
private fun StatisticByPeriodDetailsPopupContent(budget: Budget, totalAmount: Double) {
    val usedPercentage by remember {
        derivedStateOf { 100 / budget.amountLimit * totalAmount }
    }
    val pieChartPercentage by remember {
        derivedStateOf { (3.6 * usedPercentage).toFloat() }
    }
    val pieChartBrush = if (usedPercentage < 50.0) {
        GlanceTheme.greenGradient.let { listOf(it.second, it.first) }
    } else if (usedPercentage >= 50.0 && usedPercentage < 100.0) {
        GlanceTheme.yellowGradient.let { listOf(it.second, it.first) }
    } else {
        GlanceTheme.redGradient.let { listOf(it.second, it.first) }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                GlanceSingleValuePieChart(
                    percentage = pieChartPercentage,
                    brush = pieChartBrush,
                    size = 70.dp
                )
                Text(
                    text = "${usedPercentage.toInt()}%",
                    color = GlanceTheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(text = stringResource(R.string.of_limit_used))
                Text(
                    text = "(${totalAmount.formatWithSpaces(budget.currency)})",
                    color = GlanceTheme.primary,
                    fontSize = 18.sp,
                )
            }
        }
    }
}


@Preview(locale = "en")
@Composable
fun BudgetStatisticsScreenPreview() {
    val context = LocalContext.current
    val appTheme = AppTheme.LightDefault
    val category = DefaultCategoriesPackage(LocalContext.current).getDefaultCategories()
        .expense[0].category
    val budget = Budget(
        id = 1,
        priorityNum = 1.0,
        amountLimit = 4000.0,
        usedAmount = 2500.0,
        usedPercentage = 62.5F,
        category = category,
        name = category.name,
        repeatingPeriod = RepeatingPeriod.Monthly,
        dateRange = RepeatingPeriod.Monthly.getLongDateRangeWithTime(),
        currency = "USD",
        linkedAccountsIds = listOf(1, 2)
    )
    val totalAmountsByRanges = budget.repeatingPeriod.getPrevDateRanges()
        .mapIndexed { index, dateRange ->
            TotalAmountByRange(dateRange = dateRange, totalAmount = 5000.0 / (index + 1))
        }
        .let { totalAmountsByRanges ->
            budget.let { listOf(TotalAmountByRange(it.dateRange, it.usedAmount)) } +
                    totalAmountsByRanges
        }
        .reversed()
    val columnChartUiState = ColumnChartUiState.createAsBudgetStatistics(
        totalAmountsByRanges = totalAmountsByRanges,
        rowsCount = 5,
        repeatingPeriod = budget.repeatingPeriod,
        context = context
    )
    val accountList = listOf(
        Account(
            id = 1,
            orderNum = 1,
            name = "Main USD",
            currency = "USD",
            balance = 112.13,
            color = AccountPossibleColors().default.toAccountColorWithName(),
            isActive = false
        ),
        Account(
            id = 2,
            orderNum = 2,
            name = "Local Card CZK",
            currency = "CZK",
            balance = 1412.13,
            color = AccountPossibleColors().pink.toAccountColorWithName(),
            isActive = false
        ),
    )

    PreviewContainer(appTheme) {
        BudgetStatisticsScreen(
            appTheme = appTheme,
            budget = budget,
            columnChartUiState = columnChartUiState,
            budgetAccounts = accountList,
            onBackButtonClick = {}
        )
    }
}