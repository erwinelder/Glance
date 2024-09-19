package com.ataglance.walletglance.core.presentation.components.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.presentation.components.BudgetWithStatsComponent
import com.ataglance.walletglance.core.presentation.components.containers.MessageContainer

@Composable
fun ChosenWidgetsWidget(
    budgetList: List<Budget>,
    onSettingsButtonClick: () -> Unit,
    onNavigateToBudgetsScreen: () -> Unit,
    onNavigateToBudgetStatisticsScreen: (Int) -> Unit
) {
    WidgetWithTitleSettingsAndButtonComponent(
        title = stringResource(R.string.budgets),
        onSettingsButtonClick = onSettingsButtonClick,
        onBottomNavigationButtonClick = onNavigateToBudgetsScreen
    ) {
        AnimatedContent(
            targetState = budgetList,
            label = "chosen budgets on widget"
        ) { chosenBudgets ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (chosenBudgets.isNotEmpty()) {
                    chosenBudgets.forEach { budget ->
                        BudgetWithStatsComponent(
                            budget = budget,
                            onClick = {
                                onNavigateToBudgetStatisticsScreen(it.id)
                            }
                        )
                    }
                } else {
                    MessageContainer(
                        message = stringResource(R.string.you_have_not_chosen_any_budgets)
                    )
                }
            }
        }
    }
}