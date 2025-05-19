package com.ataglance.walletglance.core.presentation.component.widget

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.presentation.component.BudgetWithStatsComponent
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetsOnWidgetViewModel
import com.ataglance.walletglance.core.presentation.component.container.MessageContainer
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChosenBudgetsWidget(
    onSettingsButtonClick: () -> Unit,
    onNavigateToBudgetsScreen: () -> Unit,
    onNavigateToBudgetStatisticsScreen: (Int) -> Unit
) {
    val resourceManager = koinInject<ResourceManager>()
    val viewModel = koinViewModel<BudgetsOnWidgetViewModel>()

    val budgets by viewModel.budgets.collectAsStateWithLifecycle()

    ChosenBudgetsWidgetContent(
        budgets = budgets,
        resourceManager = resourceManager,
        onSettingsButtonClick = onSettingsButtonClick,
        onNavigateToBudgetsScreen = onNavigateToBudgetsScreen,
        onNavigateToBudgetStatisticsScreen = onNavigateToBudgetStatisticsScreen
    )
}

@Composable
private fun ChosenBudgetsWidgetContent(
    budgets: List<Budget>,
    resourceManager: ResourceManager,
    onSettingsButtonClick: () -> Unit,
    onNavigateToBudgetsScreen: () -> Unit,
    onNavigateToBudgetStatisticsScreen: (Int) -> Unit
) {
    WidgetWithTitleSettingsAndButtonComponent(
        title = stringResource(R.string.budgets),
        onSettingsButtonClick = onSettingsButtonClick,
        onBottomNavigationButtonClick = onNavigateToBudgetsScreen
    ) {
        AnimatedContent(targetState = budgets) { chosenBudgets ->
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
                            },
                            resourceManager = resourceManager,
                            showDateRangeLabels = true
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