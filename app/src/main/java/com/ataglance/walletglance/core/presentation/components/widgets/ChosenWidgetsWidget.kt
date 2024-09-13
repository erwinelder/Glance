package com.ataglance.walletglance.core.presentation.components.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.budget.domain.Budget
import com.ataglance.walletglance.budget.presentation.components.BudgetComponent

@Composable
fun ChosenWidgetsWidget(
    budgetList: List<Budget>,
    onNavigateToBudgetsScreen: () -> Unit,
    onNavigateToBudgetStatisticsScreen: (Int) -> Unit
) {
    WidgetWithTitleAndButtonComponent(
        title = stringResource(R.string.budgets),
        onBottomNavigationButtonClick = onNavigateToBudgetsScreen
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            budgetList.forEach { budget ->
                BudgetComponent(
                    budget = budget,
                    onClick = {
                        onNavigateToBudgetStatisticsScreen(it.id)
                    }
                )
            }
        }
    }
}