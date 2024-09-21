package com.ataglance.walletglance.budget.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.presentation.components.dividers.TextDivider
import com.ataglance.walletglance.core.utils.asStringRes

@Composable
fun <T> BudgetTypeListComponent(
    budgetList: List<T>,
    repeatingPeriod: RepeatingPeriod,
    budgetComponent: @Composable (T) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextDivider(
            modifier = Modifier.fillMaxWidth(.9f),
            textRes = repeatingPeriod.asStringRes()
        )
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            budgetList.forEach {
                budgetComponent(it)
            }
        }
    }
}