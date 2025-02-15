package com.ataglance.walletglance.category.presentation.components

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.presentation.model.CategoryStatistics
import com.ataglance.walletglance.category.presentation.screen.CategoryStatisticsScreenPreview
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.components.charts.GlanceLineChart
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceOnGlassSurface

@Composable
fun CategoryStatisticsItemComponent(
    uiState: CategoryStatistics?,
    showLeftArrow: Boolean = false,
    enableClick: Boolean? = null,
    onClick: () -> Unit = {}
) {
    GlassSurfaceOnGlassSurface(
        modifier = Modifier.alpha(if (uiState == null) 0f else 1f),
        onClick = onClick,
        clickEnabled = enableClick == null &&
                uiState != null && (
                    uiState.subcategoriesStatistics != null || showLeftArrow
                ) || enableClick == true,
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
                if (showLeftArrow) {
                    Icon(
                        painter = painterResource(R.drawable.short_arrow_left_icon),
                        contentDescription = "go back to all categories",
                        tint = GlanceColors.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
                uiState?.category?.let {
                    CategoryIconComponent(category = it)
                }
                Text(
                    text = uiState?.category?.name ?: "---",
                    color = GlanceColors.onSurface,
                    fontSize = 19.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                if (uiState?.subcategoriesStatistics != null && !showLeftArrow) {
                    Icon(
                        painter = painterResource(R.drawable.short_arrow_right_icon),
                        contentDescription = "go to ${uiState.category.name} subcategories",
                        tint = GlanceColors.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = uiState?.totalAmount ?: "---",
                            color = GlanceColors.onSurface,
                            fontSize = 18.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Text(
                            text = uiState?.currency ?: "",
                            color = GlanceColors.onSurface.copy(.6f),
                            fontSize = 17.sp
                        )
                    }
                    Text(
                        text = uiState?.percentageFormatted ?: "---",
                        color = GlanceColors.onSurface,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                uiState?.let {
                    GlanceLineChart(
                        percentage = uiState.percentageFloat,
                        brushColors = uiState.category.getLineChartColorsByTheme(CurrAppTheme),
                        shadowColor = uiState.category.getIconSolidColorByTheme(CurrAppTheme)
                    )
                }
            }
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun CategoryStatisticsItemComponentPreview() {
    CategoryStatisticsScreenPreview()
}
