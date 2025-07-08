package com.ataglance.walletglance.budget.presentation.component

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
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
    textDividerFilledWidth: Float = .9f,
    budgetComponent: @Composable (Budget) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        budgetsByType.iterateByType { repeatingPeriod, budgetList ->
            item {
                BudgetTypeListComponent(
                    budgetList = budgetList,
                    repeatingPeriod = repeatingPeriod,
                    textDividerFilledWidth = textDividerFilledWidth,
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