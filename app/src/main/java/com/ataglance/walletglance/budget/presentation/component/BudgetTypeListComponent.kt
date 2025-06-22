package com.ataglance.walletglance.budget.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.presentation.component.divider.TextDivider
import com.ataglance.walletglance.core.domain.date.asStringRes

@Composable
fun <T> BudgetTypeListComponent(
    budgetList: List<T>,
    repeatingPeriod: RepeatingPeriod,
    textDividerFilledWidth: Float = .9f,
    budgetComponent: @Composable (T) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextDivider(
            modifier = Modifier.fillMaxWidth(textDividerFilledWidth),
            textRes = repeatingPeriod.asStringRes()
        )
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            budgetList.forEach {
                budgetComponent(it)
            }
        }
    }
}