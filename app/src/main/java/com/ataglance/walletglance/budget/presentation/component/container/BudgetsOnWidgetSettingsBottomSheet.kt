package com.ataglance.walletglance.budget.presentation.component.container

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.mapper.budget.toCheckedBudgetsByType
import com.ataglance.walletglance.budget.presentation.component.CheckedBudgetListsByPeriodComponent
import com.ataglance.walletglance.budget.presentation.component.CheckedDefaultBudgetComponent
import com.ataglance.walletglance.budget.presentation.model.CheckedBudgetsByType
import com.ataglance.walletglance.budget.presentation.viewmodel.BudgetsOnWidgetSettingsViewModel
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.presentation.component.bottomSheet.BottomSheetComponent
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.utils.getLongDateRangeWithTime
import com.ataglance.walletglance.personalization.domain.model.WidgetName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetsOnWidgetSettingsBottomSheet(
    viewModel: BudgetsOnWidgetSettingsViewModel
) {
    val sheetState = rememberModalBottomSheetState()

    val openedWidgetSettings by viewModel.openedWidgetSettings.collectAsStateWithLifecycle()

    val checkedBudgetByType by viewModel.checkedBudgetsByType.collectAsStateWithLifecycle()
    val limitIsReached by viewModel.checkedBudgetsLimitIsReached.collectAsStateWithLifecycle()

    BottomSheetComponent(
        visible = openedWidgetSettings == WidgetName.ChosenBudgets,
        sheetState = sheetState,
        onDismissRequest = {
            viewModel.saveCurrentBudgetsOnWidget()
            viewModel.closeWidgetSettings()
        },
    ) {
        BudgetsOnWidgetSettingsBottomSheetContent(
            checkedBudgetsByType = checkedBudgetByType,
            limitIsReached = limitIsReached,
            onCheckBudget = viewModel::checkBudgetOnWidget,
            onUncheckBudget = viewModel::uncheckBudgetOnWidget
        )
    }
}

@Composable
private fun BudgetsOnWidgetSettingsBottomSheetContent(
    checkedBudgetsByType: CheckedBudgetsByType,
    limitIsReached: Boolean,
    onCheckBudget: (Int) -> Unit,
    onUncheckBudget: (Int) -> Unit
) {
    CheckedBudgetListsByPeriodComponent(budgetsByType = checkedBudgetsByType) { checkedBudget ->
        val alpha by animateFloatAsState(
            targetValue = if (!limitIsReached || checkedBudget.checked) 1f else 0.5f
        )
        CheckedDefaultBudgetComponent(
            budget = checkedBudget.budget,
            modifier = Modifier.alpha(alpha),
            checked = checkedBudget.checked,
            checkedEnabled = !limitIsReached || checkedBudget.checked
        ) {
            if (checkedBudget.checked) {
                onUncheckBudget(checkedBudget.budget.id)
            } else {
                onCheckBudget(checkedBudget.budget.id)
            }
        }
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun BudgetsOnWidgetSettingsBottomSheetPreview(
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
) {
    val checkedBudgetsByType = BudgetsByType(
        daily = listOf(
            Budget(
                id = 1,
                priorityNum = 1.0,
                amountLimit = 4000.0,
                usedAmount = 2500.0,
                usedPercentage = 62.5F,
                category = groupedCategoriesByType.expense[0].category,
                name = "Food & drinks",
                repeatingPeriod = RepeatingPeriod.Daily,
                dateRange = RepeatingPeriod.Daily.getLongDateRangeWithTime(),
                currentTimeWithinRangeGraphPercentage = .5f,
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
                category = groupedCategoriesByType.expense[1].category,
                name = "Housing",
                repeatingPeriod = RepeatingPeriod.Weekly,
                dateRange = RepeatingPeriod.Weekly.getLongDateRangeWithTime(),
                currentTimeWithinRangeGraphPercentage = .5f,
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
                category = groupedCategoriesByType.expense[0].category,
                name = "Food & drinks",
                repeatingPeriod = RepeatingPeriod.Monthly,
                dateRange = RepeatingPeriod.Monthly.getLongDateRangeWithTime(),
                currentTimeWithinRangeGraphPercentage = .5f,
                currency = "USD",
                linkedAccountsIds = listOf(1, 2)
            ),
            Budget(
                id = 3,
                priorityNum = 3.0,
                amountLimit = 4000.0,
                usedAmount = 1000.0,
                usedPercentage = 25F,
                category = groupedCategoriesByType.expense[2].category,
                name = "Shopping",
                repeatingPeriod = RepeatingPeriod.Monthly,
                dateRange = RepeatingPeriod.Monthly.getLongDateRangeWithTime(),
                currentTimeWithinRangeGraphPercentage = .5f,
                currency = "CZK",
                linkedAccountsIds = listOf(3, 4)
            )
        )
    )
        .toCheckedBudgetsByType(listOf(1, 2))
    val limitIsReached = true

    PreviewContainer {
        BudgetsOnWidgetSettingsBottomSheetContent(
            checkedBudgetsByType = checkedBudgetsByType,
            limitIsReached = limitIsReached,
            onCheckBudget = {},
            onUncheckBudget = {}
        )
    }
}