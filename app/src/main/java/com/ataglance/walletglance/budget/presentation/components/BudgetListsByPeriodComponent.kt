package com.ataglance.walletglance.budget.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.budget.domain.Budget
import com.ataglance.walletglance.budget.domain.BudgetsByType
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.utils.asStringRes
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetsScreenPreview
import com.ataglance.walletglance.core.presentation.components.dividers.TextDivider

@Composable
fun BudgetListsByPeriodComponent(
    budgetsByType: BudgetsByType,
    budgetComponent: @Composable (Budget) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        budgetsByType.daily.takeIf { it.isNotEmpty() }?.let { budgetList ->
            item {
                BudgetListByTypeComponent(
                    budgetList = budgetList,
                    repeatingPeriod = RepeatingPeriod.Daily,
                    budgetComponent = budgetComponent
                )
            }
        }
        budgetsByType.weekly.takeIf { it.isNotEmpty() }?.let { budgetList ->
            item {
                BudgetListByTypeComponent(
                    budgetList = budgetList,
                    repeatingPeriod = RepeatingPeriod.Weekly,
                    budgetComponent = budgetComponent
                )
            }
        }
        budgetsByType.monthly.takeIf { it.isNotEmpty() }?.let { budgetList ->
            item {
                BudgetListByTypeComponent(
                    budgetList = budgetList,
                    repeatingPeriod = RepeatingPeriod.Monthly,
                    budgetComponent = budgetComponent
                )
            }
        }
        budgetsByType.yearly.takeIf { it.isNotEmpty() }?.let { budgetList ->
            item {
                BudgetListByTypeComponent(
                    budgetList = budgetList,
                    repeatingPeriod = RepeatingPeriod.Yearly,
                    budgetComponent = budgetComponent
                )
            }
        }
    }
}

@Composable
private fun BudgetListByTypeComponent(
    budgetList: List<Budget>,
    repeatingPeriod: RepeatingPeriod,
    budgetComponent: @Composable (Budget) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextDivider(
            modifier = Modifier.fillMaxWidth(.9f),
            textRes = repeatingPeriod.asStringRes()
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            budgetList.forEach {
                budgetComponent(it)
            }
        }
    }
}


@Preview
@Composable
private fun Preview() {
    EditBudgetsScreenPreview()
}