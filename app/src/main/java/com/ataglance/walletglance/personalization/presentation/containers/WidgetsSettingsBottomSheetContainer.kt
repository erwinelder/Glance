package com.ataglance.walletglance.personalization.presentation.containers

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.budget.mapper.toCheckedBudgetsByType
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.presentation.components.CheckedBudgetListsByPeriodComponent
import com.ataglance.walletglance.budget.presentation.components.CheckedDefaultBudgetComponent
import com.ataglance.walletglance.core.presentation.components.containers.GlanceBottomSheet
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetsSettingsBottomSheetContainer(
    personalizationViewModel: PersonalizationViewModel,
    budgetsByType: BudgetsByType,
    budgetsOnWidget: List<Budget>
) {
    val sheetState = rememberModalBottomSheetState()

    val openedWidgetSettings by personalizationViewModel.openedWidgetSettings
        .collectAsStateWithLifecycle()

    val checkedBudgetByType by remember(budgetsByType, budgetsOnWidget) {
        derivedStateOf {
            budgetsByType.toCheckedBudgetsByType(budgetsOnWidget)
        }
    }
    val checkedEnabled by remember(checkedBudgetByType) {
        derivedStateOf {
            checkedBudgetByType.countCheckedBudgets() < 3
        }
    }

    GlanceBottomSheet(
        visible = openedWidgetSettings == WidgetName.ChosenBudgets,
        sheetState = sheetState,
        onDismissRequest = {
            personalizationViewModel.saveCurrentBudgetsOnWidgetToDb()
            personalizationViewModel.closeWidgetSettings()
        },
    ) {
        CheckedBudgetListsByPeriodComponent(budgetsByType = checkedBudgetByType) { checkedBudget ->
            val alpha by animateFloatAsState(
                targetValue = if (checkedEnabled || checkedBudget.checked) 1f else 0.5f,
                label = "alpha"
            )
            CheckedDefaultBudgetComponent(
                budget = checkedBudget.budget,
                modifier = Modifier.alpha(alpha),
                checked = checkedBudget.checked,
                checkedEnabled = checkedEnabled || checkedBudget.checked
            ) {
                if (checkedBudget.checked) {
                    personalizationViewModel.uncheckBudgetOnWidget(checkedBudget.budget.id)
                } else {
                    personalizationViewModel.checkBudgetOnWidget(checkedBudget.budget.id)
                }
            }
        }
    }
}