package com.ataglance.walletglance.budget.presentation.screen

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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.presentation.components.AccountsFlowRow
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.TotalAmountByRange
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.presentation.components.CategoryBigIconComponent
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.statistics.ColumnChartUiState
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.components.charts.GlanceColumnChart
import com.ataglance.walletglance.core.presentation.components.charts.GlanceSingleValuePieChart
import com.ataglance.walletglance.core.presentation.components.containers.BackButtonBlock
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.core.utils.formatWithSpaces
import com.ataglance.walletglance.core.utils.getLongDateRangeWithTime
import com.ataglance.walletglance.core.utils.getPrevDateRanges
import com.ataglance.walletglance.core.utils.getSpendingInRecentStringRes

@Composable
fun BudgetStatisticsScreen(
    budget: Budget,
    columnChartUiState: ColumnChartUiState,
    budgetAccounts: List<Account>,
    onBackButtonClick: () -> Unit
) {
    val nestedScrollInterop = rememberNestedScrollInteropConnection()
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
                        CategoryBigIconComponent(category = it)
                    }
                    Text(
                        text = budget.name,
                        color = GlanceColors.onSurface,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(
                            R.string.amount_currency_spending_limit,
                            budget.amountLimit.formatWithSpaces(), budget.currency
                        ),
                        color = GlanceColors.onSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    AccountsFlowRow(accountList = budgetAccounts, maxLines = 5)
                }
            }
            item { Spacer(modifier = Modifier.height(verticalGap)) }
            item {
                GlanceColumnChart(
                    uiState = columnChartUiState,
                    columnsColor = budget.category?.getColorByTheme(CurrAppTheme)?.lighter,
                    title = stringResource(
                        id = budget.repeatingPeriod.getSpendingInRecentStringRes(), budget.currency
                    ),
                    bottomNote = "Average spending: " +
                            columnChartUiState.averageValue.formatWithSpaces(budget.currency)
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
    val usedPercentage by remember(budget, totalAmount) {
        derivedStateOf { 100 / budget.amountLimit * totalAmount }
    }
    val pieChartPercentage by remember(usedPercentage) {
        derivedStateOf { (3.6 * usedPercentage).toFloat() }
    }
    val pieChartBrush = if (usedPercentage < 50.0) {
        GlanceColors.pieChartGreenGradient.reversed()
    } else if (usedPercentage >= 50.0 && usedPercentage < 100.0) {
        GlanceColors.pieChartYellowGradient.reversed()
    } else {
        GlanceColors.pieChartRedGradient.reversed()
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
                    color = GlanceColors.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = stringResource(R.string.of_limit_used),
                    color = GlanceColors.onSurface,
                )
                Text(
                    text = "(${totalAmount.formatWithSpaces(budget.currency)})",
                    color = GlanceColors.onSurface,
                    fontSize = 18.sp,
                )
            }
        }
    }
}


@Preview(
    locale = "en",
    device = Devices.PIXEL_7_PRO,
)
@Composable
fun BudgetStatisticsScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    isBottomBarVisible: Boolean = true,
    categoriesWithSubcategories: CategoriesWithSubcategories = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    accountList: List<Account> = listOf(
        Account(
            id = 1,
            orderNum = 1,
            name = "Main USD",
            currency = "USD",
            balance = 112.13,
            color = AccountColors.Default,
            isActive = false
        ),
        Account(
            id = 2,
            orderNum = 2,
            name = "Local Card CZK",
            currency = "CZK",
            balance = 1412.13,
            color = AccountColors.Pink,
            isActive = false
        ),
    ),
    budget: Budget = Budget(
        id = 1,
        priorityNum = 1.0,
        amountLimit = 4000.0,
        usedAmount = 2500.0,
        usedPercentage = 62.5F,
        category = categoriesWithSubcategories.expense[0].category,
        name = categoriesWithSubcategories.expense[0].category.name,
        repeatingPeriod = RepeatingPeriod.Monthly,
        dateRange = RepeatingPeriod.Monthly.getLongDateRangeWithTime(),
        currentTimeWithinRangeGraphPercentage = .5f,
        currency = "USD",
        linkedAccountsIds = listOf(1, 2)
    ),
    totalAmounts: List<Double> = (0..5).map { 5000.0 / (it + 1) }
) {
    val context = LocalContext.current
    val totalAmountsByRanges = budget.repeatingPeriod.getPrevDateRanges()
        .mapIndexed { index, dateRange ->
            TotalAmountByRange(
                dateRange = dateRange,
                totalAmount = totalAmounts.getOrNull(index) ?: 0.0
            )
        }
        .let { totalAmountsByRanges ->
            listOf(TotalAmountByRange(budget.dateRange, budget.usedAmount)) + totalAmountsByRanges
        }
        .reversed()
    val columnChartUiState = ColumnChartUiState.createAsBudgetStatistics(
        totalAmountsByRanges = totalAmountsByRanges,
        rowsCount = 5,
        repeatingPeriod = budget.repeatingPeriod,
        context = context
    )

    PreviewContainer(appTheme = appTheme) {
        BudgetStatisticsScreen(
            budget = budget,
            columnChartUiState = columnChartUiState,
            budgetAccounts = accountList,
            onBackButtonClick = {}
        )
    }
}