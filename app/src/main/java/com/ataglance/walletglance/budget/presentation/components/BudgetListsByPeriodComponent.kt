package com.ataglance.walletglance.budget.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetsScreenPreview

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
        budgetsByType.iterateByType { repeatingPeriod, budgetList ->
            item {
                BudgetTypeListComponent(
                    budgetList = budgetList,
                    repeatingPeriod = repeatingPeriod,
                    budgetComponent = budgetComponent
                )
            }
        }
    }
}



@Preview
@Composable
private fun Preview() {
    EditBudgetsScreenPreview()
}