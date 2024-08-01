package com.ataglance.walletglance.ui.theme.screens.settings.budgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.budgets.BudgetRepeatingPeriod
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.utils.getTodayDateLong
import com.ataglance.walletglance.ui.theme.screencontainers.SetupDataScreenContainer
import com.ataglance.walletglance.ui.theme.uielements.budgets.EditingBudgetComponent
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.SmallPrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.containers.PreviewContainer

@Composable
fun EditBudgetScreen(
    scaffoldPadding: PaddingValues,
    appTheme: AppTheme?,
    budgetList: List<Budget>,
    onNavigateToEditBudgetScreen: (Budget?) -> Unit,
    onSaveBudgetsButton: () -> Unit,
) {
    SetupDataScreenContainer(
        topPadding = scaffoldPadding.calculateTopPadding(),
        glassSurfaceContent = {
            GlassSurfaceContent(
                appTheme = appTheme,
                budgetList = budgetList,
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
    budgetList: List<Budget>,
    onNavigateToEditBudgetScreen: (Budget?) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = budgetList, key = { it.id }) { budget ->
            EditingBudgetComponent(
                appTheme = appTheme,
                budget = budget,
                onClick = onNavigateToEditBudgetScreen
            )
        }
    }
}


@Preview
@Composable
fun EditBudgetScreenPreview() {
    val context = LocalContext.current
    val appTheme = AppTheme.LightDefault
    val budgetList = listOf(
        Budget(
            id = 1,
            amountLimit = 4000.0,
            usedAmount = 2500.0,
            usedPercentage = 62.5F,
            category = DefaultCategoriesPackage(context).getDefaultCategories().expense[0].category,
            name = "Food & drinks",
            repeatingPeriod = BudgetRepeatingPeriod.OneTime,
            lastResetDate = getTodayDateLong(),
            linkedAccountsIds = listOf(1, 2)
        ),
        Budget(
            id = 2,
            amountLimit = 4000.0,
            usedAmount = 1000.0,
            usedPercentage = 25F,
            category = DefaultCategoriesPackage(context).getDefaultCategories().expense[1].category,
            name = "Housing",
            repeatingPeriod = BudgetRepeatingPeriod.OneTime,
            lastResetDate = getTodayDateLong(),
            linkedAccountsIds = listOf(1, 2)
        )
    )

    PreviewContainer {
        EditBudgetScreen(
            scaffoldPadding = PaddingValues(0.dp),
            appTheme = appTheme,
            budgetList = budgetList,
            onNavigateToEditBudgetScreen = {},
            onSaveBudgetsButton = {}
        )
    }
}