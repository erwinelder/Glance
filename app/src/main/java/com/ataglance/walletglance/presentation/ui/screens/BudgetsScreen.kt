package com.ataglance.walletglance.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.budgets.BudgetsByType
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.date.RepeatingPeriod
import com.ataglance.walletglance.domain.utils.getLongDateRangeWithTime
import com.ataglance.walletglance.presentation.ui.WindowTypeIsExpanded
import com.ataglance.walletglance.presentation.ui.uielements.budgets.BudgetComponent
import com.ataglance.walletglance.presentation.ui.uielements.budgets.BudgetListsByPeriodComponent
import com.ataglance.walletglance.presentation.ui.uielements.containers.GlassSurface
import com.ataglance.walletglance.presentation.ui.uielements.containers.MessageContainer
import com.ataglance.walletglance.presentation.ui.uielements.containers.PreviewContainer

@Composable
fun BudgetsScreen(
    scaffoldPadding: PaddingValues,
    appTheme: AppTheme?,
    budgetsByType: BudgetsByType,
    onBudgetClick: (Budget) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 16.dp + scaffoldPadding.calculateBottomPadding())
    ) {
        GlassSurface(
            modifier = Modifier.weight(1f),
            filledWidth = if (!WindowTypeIsExpanded) null else .86f
        ) {
            if (budgetsByType.areEmpty()) {
                MessageContainer(message = stringResource(R.string.you_have_no_budgets_yet))
            } else {
                BudgetListsByPeriodComponent(budgetsByType) { budget ->
                    BudgetComponent(appTheme = appTheme, budget = budget, onClick = onBudgetClick)
                }
            }
        }
    }
}


@Preview
@Composable
fun BudgetsScreenPreview() {
    val context = LocalContext.current
    val appTheme = AppTheme.LightDefault
    val defaultCategories = DefaultCategoriesPackage(context).getDefaultCategories()
    val budgetsByType = BudgetsByType(
        daily = listOf(
            Budget(
                id = 1,
                priorityNum = 1.0,
                amountLimit = 4000.0,
                usedAmount = 2500.0,
                usedPercentage = 62.5F,
                category = defaultCategories.expense[0].category,
                name = "Food & drinks",
                repeatingPeriod = RepeatingPeriod.Daily,
                dateRange = RepeatingPeriod.Daily.getLongDateRangeWithTime(),
                currency = "USD",
                linkedAccountsIds = listOf(1, 2)
            )
        ),
        weekly = listOf(
            Budget(
                id = 2,
                priorityNum = 2.0,
                amountLimit = 4000.0,
                usedAmount = 1000.0,
                usedPercentage = 25F,
                category = defaultCategories.expense[1].category,
                name = "Housing",
                repeatingPeriod = RepeatingPeriod.Weekly,
                dateRange = RepeatingPeriod.Weekly.getLongDateRangeWithTime(),
                currency = "CZK",
                linkedAccountsIds = listOf(3, 4)
            )
        ),
        monthly = listOf(
            Budget(
                id = 1,
                priorityNum = 1.0,
                amountLimit = 4000.0,
                usedAmount = 2500.0,
                usedPercentage = 62.5F,
                category = defaultCategories.expense[0].category,
                name = "Food & drinks",
                repeatingPeriod = RepeatingPeriod.Monthly,
                dateRange = RepeatingPeriod.Monthly.getLongDateRangeWithTime(),
                currency = "USD",
                linkedAccountsIds = listOf(1, 2)
            ),
            Budget(
                id = 3,
                priorityNum = 3.0,
                amountLimit = 4000.0,
                usedAmount = 1000.0,
                usedPercentage = 25F,
                category = defaultCategories.expense[2].category,
                name = "Shopping",
                repeatingPeriod = RepeatingPeriod.Monthly,
                dateRange = RepeatingPeriod.Monthly.getLongDateRangeWithTime(),
                currency = "CZK",
                linkedAccountsIds = listOf(3, 4)
            )
        )
    )

    PreviewContainer {
        BudgetsScreen(
            scaffoldPadding = PaddingValues(),
            appTheme = appTheme,
            budgetsByType = budgetsByType,
            onBudgetClick = {}
        )
    }
}