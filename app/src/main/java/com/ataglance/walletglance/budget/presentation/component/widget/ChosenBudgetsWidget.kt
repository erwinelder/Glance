package com.ataglance.walletglance.budget.presentation.component.widget

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.presentation.component.BudgetWithStatsOnGlassComponent
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetsOnWidgetViewModel
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.presentation.component.container.MessageContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.component.widget.component.WidgetAdjustButton
import com.ataglance.walletglance.core.presentation.component.widget.component.WidgetViewAllButton
import com.ataglance.walletglance.core.presentation.component.widget.container.WidgetContainer
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.model.ResourceManagerImpl
import com.ataglance.walletglance.core.utils.getLongDateRangeWithTime
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChosenBudgetsWidgetWrapper(
    onSettingsButtonClick: () -> Unit,
    onNavigateToBudgetsScreen: () -> Unit,
    onNavigateToBudgetStatisticsScreen: (Int) -> Unit
) {
    val resourceManager = koinInject<ResourceManager>()
    val viewModel = koinViewModel<BudgetsOnWidgetViewModel>()

    val budgets by viewModel.budgets.collectAsStateWithLifecycle()

    ChosenBudgetsWidget(
        budgets = budgets,
        resourceManager = resourceManager,
        onAdjustWidget = onSettingsButtonClick,
        onNavigateToBudgetsScreen = onNavigateToBudgetsScreen,
        onNavigateToBudgetStatisticsScreen = onNavigateToBudgetStatisticsScreen
    )
}

@Composable
fun ChosenBudgetsWidget(
    budgets: List<Budget>,
    resourceManager: ResourceManager,
    onAdjustWidget: () -> Unit,
    onNavigateToBudgetsScreen: () -> Unit,
    onNavigateToBudgetStatisticsScreen: (Int) -> Unit
) {
    WidgetContainer(
        title = stringResource(R.string.budgets),
        buttonsBlock = {
            WidgetAdjustButton(onClick = onAdjustWidget)
            WidgetViewAllButton(onClick = onNavigateToBudgetsScreen)
        }
    ) {
        AnimatedContent(targetState = budgets) { chosenBudgets ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (chosenBudgets.isNotEmpty()) {
                    chosenBudgets.forEach { budget ->
                        BudgetWithStatsOnGlassComponent(
                            budget = budget,
                            showDateRangeLabels = true,
                            resourceManager = resourceManager
                        ) {
                            onNavigateToBudgetStatisticsScreen(it.id)
                        }
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



@Preview(device = PIXEL_7_PRO, locale = "en")
@Composable
fun ChosenBudgetsWidgetPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    val resourceManager = ResourceManagerImpl(context = LocalContext.current)
    val groupedCategoriesByType = DefaultCategoriesPackage(context = LocalContext.current)
        .getDefaultCategories()
    val budgets = listOf(
        Budget(
            id = 1,
            priorityNum = 1.0,
            amountLimit = 4000.0,
            usedAmount = 2250.0,
            usedPercentage = 56.25f,
            category = groupedCategoriesByType.expense[0].category,
            name = groupedCategoriesByType.expense[0].category.name,
            repeatingPeriod = RepeatingPeriod.Monthly,
            dateRange = RepeatingPeriod.Monthly.getLongDateRangeWithTime(),
            currentTimeWithinRangeGraphPercentage = .5f,
            currency = "USD",
            linkedAccountsIds = listOf(1)
        ),
        Budget(
            id = 2,
            priorityNum = 2.0,
            amountLimit = 6000.0,
            usedAmount = 2250.0,
            usedPercentage = 36.25f,
            category = groupedCategoriesByType.expense[1].category,
            name = groupedCategoriesByType.expense[1].category.name,
            repeatingPeriod = RepeatingPeriod.Monthly,
            dateRange = RepeatingPeriod.Monthly.getLongDateRangeWithTime(),
            currentTimeWithinRangeGraphPercentage = .5f,
            currency = "USD",
            linkedAccountsIds = listOf(1)
        ),
    )

    PreviewContainer(appTheme = appTheme) {
        ChosenBudgetsWidget(
            budgets = budgets,
            resourceManager = resourceManager,
            onAdjustWidget = {},
            onNavigateToBudgetsScreen = {},
            onNavigateToBudgetStatisticsScreen = {}
        )
    }
}