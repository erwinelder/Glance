package com.ataglance.walletglance.budget.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.presentation.screen.BudgetsScreenPreview
import com.ataglance.walletglance.category.presentation.components.CategoryIconComponent
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.presentation.components.charts.GlanceLineChart
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.utils.formatWithSpaces
import com.ataglance.walletglance.core.utils.toStringDateRange

@Composable
fun BudgetWithStatsComponent(
    budget: Budget,
    onClick: (Budget) -> Unit,
    resourceManager: ResourceManager,
    showDateRangeLabels: Boolean = false
) {
    GlassSurfaceOnGlassSurface(
        onClick = { onClick(budget) },
        filledWidth = 1f,
        paddingValues = PaddingValues(24.dp, 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                budget.category?.let {
                    CategoryIconComponent(category = it)
                }
                Text(
                    text = budget.name,
                    color = GlanceColors.onSurface,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(R.drawable.short_arrow_right_icon),
                    contentDescription = "right arrow icon",
                    tint = GlanceColors.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = budget.usedAmount.formatWithSpaces(),
                        color = GlanceColors.onSurface,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = budget.amountLimit.formatWithSpaces(),
                            color = GlanceColors.onSurface,
                            fontSize = 18.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Text(
                            text = budget.currency,
                            color = GlanceColors.onSurface.copy(.6f),
                            fontSize = 17.sp
                        )
                    }
                }
                budget.category?.let {
                    GlanceLineChart(
                        percentage = budget.usedPercentage / 100,
                        brushColors = it.getLineChartColorsByTheme(CurrAppTheme),
                        shadowColor = it.getIconSolidColorByTheme(CurrAppTheme)
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                budget.category?.let {
                    GlanceLineChart(
                        percentage = budget.currentTimeWithinRangeGraphPercentage,
                        brushColors = it.getLineChartColorsByTheme(CurrAppTheme),
                        shadowColor = it.getIconSolidColorByTheme(CurrAppTheme),
                        height = 6.dp
                    )
                }
                if (showDateRangeLabels) {
                    DateRangeLabels(
                        dateRange = budget.dateRange,
                        repeatingPeriod = budget.repeatingPeriod,
                        resourceManager = resourceManager
                    )
                }
            }
        }
    }
}

@Composable
private fun DateRangeLabels(
    dateRange: LongDateRange,
    repeatingPeriod: RepeatingPeriod,
    resourceManager: ResourceManager
) {
    val stringDateRange by remember {
        derivedStateOf {
            dateRange.toStringDateRange(period = repeatingPeriod, resourceManager = resourceManager)
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringDateRange.from,
            color = GlanceColors.onSurface,
            fontSize = 17.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = stringDateRange.to,
            color = GlanceColors.onSurface,
            fontSize = 17.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun Preview() {
    BudgetsScreenPreview(appTheme = AppTheme.LightDefault)
}