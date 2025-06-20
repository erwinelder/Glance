package com.ataglance.walletglance.budget.presentation.component

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.presentation.screen.BudgetsScreenPreview
import com.ataglance.walletglance.category.presentation.component.CategoryIconComponent
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.presentation.component.chart.LineChartComponent
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.budget.presentation.component.widget.ChosenBudgetsWidgetPreview
import com.ataglance.walletglance.core.presentation.model.ResourceManager
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.core.utils.formatWithSpaces
import com.ataglance.walletglance.core.utils.toStringDateRange

@Composable
fun BudgetWithStatsGlassComponent(
    budget: Budget,
    showDateRangeLabels: Boolean = false,
    resourceManager: ResourceManager,
    onClick: (Budget) -> Unit
) {
    GlassSurface(
        cornerSize = 26.dp,
        contentPadding = PaddingValues(20.dp, 16.dp),
        modifier = Modifier.bounceClickEffect(.98f) { onClick(budget) }
    ) {
        BudgetWithStatsComponentContent(
            budget = budget,
            showDateRangeLabels = showDateRangeLabels,
            resourceManager = resourceManager
        )
    }
}

@Composable
fun BudgetWithStatsOnGlassComponent(
    budget: Budget,
    showDateRangeLabels: Boolean = false,
    resourceManager: ResourceManager,
    onClick: (Budget) -> Unit
) {
    GlassSurfaceOnGlassSurface(
        onClick = { onClick(budget) },
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)
    ) {
        BudgetWithStatsComponentContent(
            budget = budget,
            showDateRangeLabels = showDateRangeLabels,
            resourceManager = resourceManager
        )
    }
}


@Composable
private fun BudgetWithStatsComponentContent(
    budget: Budget,
    showDateRangeLabels: Boolean = false,
    resourceManager: ResourceManager
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            budget.category?.let {
                CategoryIconComponent(category = it, cornerSize = 34)
            }
            Text(
                text = budget.name,
                color = GlanciColors.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                fontFamily = Manrope,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(R.drawable.short_arrow_right_icon),
                contentDescription = "right arrow icon",
                tint = GlanciColors.onSurface,
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
                    color = GlanciColors.onSurface,
                    fontSize = 18.sp,
                    fontFamily = Manrope,
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
                        color = GlanciColors.onSurface,
                        fontSize = 18.sp,
                        fontFamily = Manrope,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Text(
                        text = budget.currency,
                        color = GlanciColors.onSurface.copy(.6f),
                        fontSize = 17.sp,
                        fontFamily = Manrope
                    )
                }
            }
            budget.category?.let {
                LineChartComponent(
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
                LineChartComponent(
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
            color = GlanciColors.onSurface,
            fontSize = 17.sp,
            fontFamily = Manrope,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = stringDateRange.to,
            color = GlanciColors.onSurface,
            fontSize = 17.sp,
            fontFamily = Manrope,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun BudgetWithStatsGlassComponentPreview() {
    BudgetsScreenPreview(appTheme = AppTheme.LightDefault)
}

@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun BudgetWithStatsOnGlassComponentPreview() {
    ChosenBudgetsWidgetPreview(appTheme = AppTheme.LightDefault)
}