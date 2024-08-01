package com.ataglance.walletglance.ui.theme.uielements.budgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.utils.formatWithSpaces
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.screens.settings.budgets.EditBudgetScreenPreview
import com.ataglance.walletglance.ui.theme.uielements.categories.CategoryIconComponent
import com.ataglance.walletglance.ui.theme.uielements.charts.GlanceLineChart
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurfaceOnGlassSurface

@Composable
fun EditingBudgetComponent(
    appTheme: AppTheme?,
    budget: Budget,
    onClick: (Budget) -> Unit
) {
    GlassSurfaceOnGlassSurface(
        onClick = { onClick(budget) },
        filledWidth = 1f,
        paddingValues = PaddingValues(24.dp, 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                budget.category?.let {
                    CategoryIconComponent(category = it, appTheme = appTheme)
                }
                Text(
                    text = budget.name,
                    color = GlanceTheme.onSurface,
                    fontSize = 19.sp,
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    painter = painterResource(R.drawable.short_arrow_right_icon),
                    contentDescription = "right arrow icon",
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = budget.usedAmount.formatWithSpaces(),
                        color = GlanceTheme.onSurface,
                    )
                    Text(
                        text = budget.amountLimit.formatWithSpaces(),
                        color = GlanceTheme.onSurface,
                    )
                }
                GlanceLineChart(
                    filledWidth = budget.usedPercentage / 100,
                    gradientBrushColors = budget.category?.colorWithName?.color?.getByTheme(appTheme)
                        ?.asListDarkToLight() ?: emptyList(),
                    shadowColor = budget.category?.colorWithName?.color?.getByTheme(appTheme)?.darker
                        ?: Color.Transparent
                )
            }
        }
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun EditingBudgetComponentPreview() {
    EditBudgetScreenPreview()
}