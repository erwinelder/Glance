package com.ataglance.walletglance.ui.theme.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.date.RepeatingPeriod
import com.ataglance.walletglance.data.statistics.ColumnChartItemUiState
import com.ataglance.walletglance.data.statistics.ColumnChartUiState
import com.ataglance.walletglance.data.utils.getLongDateRangeWithTime
import com.ataglance.walletglance.ui.theme.uielements.containers.PreviewContainer

@Composable
fun BudgetStatisticsScreen(
    appTheme: AppTheme?,
    budget: Budget,
    columnChartUiState: ColumnChartUiState
) {

}


@Preview
@Composable
private fun BudgetStatisticsScreenPreview() {
    val appTheme = AppTheme.LightDefault
    val budget = Budget(
        id = 1,
        priorityNum = 1.0,
        amountLimit = 4000.0,
        usedAmount = 2500.0,
        usedPercentage = 62.5F,
        category = DefaultCategoriesPackage(LocalContext.current).getDefaultCategories()
            .expense[0].category,
        name = "Food & drinks",
        repeatingPeriod = RepeatingPeriod.Daily,
        dateRange = RepeatingPeriod.Daily.getLongDateRangeWithTime(),
        currency = "USD",
        linkedAccountsIds = listOf(1, 2)
    )
    val columnChartUiState = ColumnChartUiState(
        columns = listOf(
            ColumnChartItemUiState(
                name = "first",
                popUpValue = "first value",
                percentageOnGraph = 50f
            ),
            ColumnChartItemUiState(
                name = "second",
                popUpValue = "second value",
                percentageOnGraph = 25f
            ),
            ColumnChartItemUiState(
                name = "third",
                popUpValue = "third value",
                percentageOnGraph = 75f
            ),
        ),
        horizontalLinesNames = listOf("1", "2", "3", "4", "5"),
        selectedColumnIndex = null
    )

    PreviewContainer(appTheme) {
        BudgetStatisticsScreen(
            appTheme = appTheme,
            budget = budget,
            columnChartUiState = columnChartUiState
        )
    }
}