package com.ataglance.walletglance.ui.theme.screens.settings.budgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.ataglance.walletglance.data.utils.asStringRes
import com.ataglance.walletglance.data.utils.getLongDateRangeWithTime
import com.ataglance.walletglance.ui.theme.screencontainers.SetupDataScreenContainer
import com.ataglance.walletglance.ui.theme.uielements.budgets.EditingBudgetComponent
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.SmallPrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.containers.PreviewContainer
import com.ataglance.walletglance.ui.theme.uielements.dividers.TextDivider

@Composable
fun EditBudgetsScreen(
    scaffoldPadding: PaddingValues,
    appTheme: AppTheme?,
    budgetsByType: BudgetsByType,
    onNavigateToEditBudgetScreen: (Budget?) -> Unit,
    onSaveBudgetsButton: () -> Unit,
) {
    SetupDataScreenContainer(
        topPadding = scaffoldPadding.calculateTopPadding(),
        glassSurfaceContent = {
            GlassSurfaceContent(
                appTheme = appTheme,
                budgetsByType = budgetsByType,
                onNavigateToEditBudgetScreen = onNavigateToEditBudgetScreen,
            )
        },
        smallPrimaryButton = {
            SmallPrimaryButton(
                onClick = {
                    onNavigateToEditBudgetScreen(null)
                },
                text = stringResource(R.string.add_budget)
            )
        },
        primaryBottomButton = {
            PrimaryButton(
                onClick = onSaveBudgetsButton,
                text = stringResource(R.string.save)
            )
        }
    )
}

@Composable
private fun GlassSurfaceContent(
    appTheme: AppTheme?,
    budgetsByType: BudgetsByType,
    onNavigateToEditBudgetScreen: (Budget?) -> Unit,
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        item {
            budgetsByType.daily.takeIf { it.isNotEmpty() }?.let { budgetList ->
                BudgetListByTypeComponent(
                    appTheme = appTheme,
                    budgetList = budgetList,
                    repeatingPeriod = RepeatingPeriod.Daily,
                    onNavigateToEditBudgetScreen = onNavigateToEditBudgetScreen
                )
            }
        }
        item {
            budgetsByType.weekly.takeIf { it.isNotEmpty() }?.let { budgetList ->
                BudgetListByTypeComponent(
                    appTheme = appTheme,
                    budgetList = budgetList,
                    repeatingPeriod = RepeatingPeriod.Weekly,
                    onNavigateToEditBudgetScreen = onNavigateToEditBudgetScreen
                )
            }
        }
        item {
            budgetsByType.monthly.takeIf { it.isNotEmpty() }?.let { budgetList ->
                BudgetListByTypeComponent(
                    appTheme = appTheme,
                    budgetList = budgetList,
                    repeatingPeriod = RepeatingPeriod.Monthly,
                    onNavigateToEditBudgetScreen = onNavigateToEditBudgetScreen
                )
            }
        }
        item {
            budgetsByType.yearly.takeIf { it.isNotEmpty() }?.let { budgetList ->
                BudgetListByTypeComponent(
                    appTheme = appTheme,
                    budgetList = budgetList,
                    repeatingPeriod = RepeatingPeriod.Yearly,
                    onNavigateToEditBudgetScreen = onNavigateToEditBudgetScreen
                )
            }
        }
    }
}

@Composable
private fun BudgetListByTypeComponent(
    appTheme: AppTheme?,
    budgetList: List<Budget>,
    repeatingPeriod: RepeatingPeriod,
    onNavigateToEditBudgetScreen: (Budget?) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextDivider(
            modifier = Modifier.fillMaxWidth(.95f),
            textRes = repeatingPeriod.asStringRes()
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            budgetList.forEach { budget ->
                EditingBudgetComponent(
                    appTheme = appTheme,
                    budget = budget,
                    onClick = onNavigateToEditBudgetScreen
                )
            }
        }
    }
}


@Preview
@Composable
fun EditBudgetsScreenPreview() {
    val context = LocalContext.current
    val appTheme = AppTheme.LightDefault
    val budgetsByType = BudgetsByType(
        daily = listOf(
            Budget(
                id = 1,
                priorityNum = 1.0,
                amountLimit = 4000.0,
                usedAmount = 2500.0,
                usedPercentage = 62.5F,
                category = DefaultCategoriesPackage(context).getDefaultCategories().expense[0].category,
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
                category = DefaultCategoriesPackage(context).getDefaultCategories().expense[1].category,
                name = "Housing",
                repeatingPeriod = RepeatingPeriod.Weekly,
                dateRange = RepeatingPeriod.Weekly.getLongDateRangeWithTime(),
                currency = "CZK",
                linkedAccountsIds = listOf(3, 4)
            )
        ),
        monthly = listOf(
            Budget(
                id = 3,
                priorityNum = 3.0,
                amountLimit = 4000.0,
                usedAmount = 1000.0,
                usedPercentage = 25F,
                category = DefaultCategoriesPackage(context).getDefaultCategories().expense[2].category,
                name = "Shopping",
                repeatingPeriod = RepeatingPeriod.Monthly,
                dateRange = RepeatingPeriod.Monthly.getLongDateRangeWithTime(),
                currency = "CZK",
                linkedAccountsIds = listOf(3, 4)
            ),
            Budget(
                id = 1,
                priorityNum = 1.0,
                amountLimit = 4000.0,
                usedAmount = 2500.0,
                usedPercentage = 62.5F,
                category = DefaultCategoriesPackage(context).getDefaultCategories().expense[0].category,
                name = "Food & drinks",
                repeatingPeriod = RepeatingPeriod.Monthly,
                dateRange = RepeatingPeriod.Monthly.getLongDateRangeWithTime(),
                currency = "USD",
                linkedAccountsIds = listOf(1, 2)
            )
        )
    )

    PreviewContainer {
        EditBudgetsScreen(
            scaffoldPadding = PaddingValues(0.dp),
            appTheme = appTheme,
            budgetsByType = budgetsByType,
            onNavigateToEditBudgetScreen = {},
            onSaveBudgetsButton = {}
        )
    }
}