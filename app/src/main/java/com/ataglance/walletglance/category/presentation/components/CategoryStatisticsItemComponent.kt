package com.ataglance.walletglance.category.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.CategoryStatisticsElementUiState
import com.ataglance.walletglance.category.presentation.screen.CategoryStatisticsScreenPreview
import com.ataglance.walletglance.core.presentation.CurrAppTheme
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.components.charts.GlanceLineChart
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceOnGlassSurface

@Composable
fun CategoryStatisticsItemComponent(
    uiState: CategoryStatisticsElementUiState?,
    showLeftArrow: Boolean = false,
    enableClick: Boolean? = null,
    onClick: () -> Unit = {}
) {
    GlassSurfaceOnGlassSurface(
        modifier = Modifier.alpha(if (uiState == null) 0f else 1f),
        onClick = onClick,
        clickEnabled = enableClick == null &&
                uiState != null && (
                    uiState.subcategoriesStatisticsUiState != null || showLeftArrow
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
                        tint = GlanceTheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
                uiState?.categoryIconRes?.let {
                    Icon(
                        painter = painterResource(it),
                        contentDescription = "category ${uiState.categoryName} icon",
                        tint = GlanceTheme.surface,
                        modifier = Modifier
                            .shadow(
                                elevation = 8.dp,
                                spotColor = uiState.categoryColor
                                    .getCategoryIconSolidColorByTheme(CurrAppTheme),
                                shape = RoundedCornerShape(30)
                            )
                            .clip(RoundedCornerShape(30))
                            .background(
                                uiState.categoryColor.getCategoryIconSolidColorByTheme(CurrAppTheme)
                            )
                            .size(32.dp)
                            .padding(5.dp)
                    )
                }
                Text(
                    text = uiState?.categoryName ?: "---",
                    color = GlanceTheme.onSurface,
                    fontSize = 19.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                if (uiState?.subcategoriesStatisticsUiState != null && !showLeftArrow) {
                    Icon(
                        painter = painterResource(R.drawable.short_arrow_right_icon),
                        contentDescription = "go to ${uiState.categoryName} subcategories",
                        tint = GlanceTheme.onSurface,
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
                            color = GlanceTheme.onSurface,
                            fontSize = 18.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Text(
                            text = uiState?.currency ?: "",
                            color = GlanceTheme.onSurface.copy(.6f),
                            fontSize = 17.sp
                        )
                    }
                    Text(
                        text = uiState?.percentageFormatted ?: "---",
                        color = GlanceTheme.onSurface,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                uiState?.let {
                    GlanceLineChart(
                        percentage = uiState.percentageFloat,
                        brushColors = uiState.categoryColor
                            .getCategoryLineChartColorsByTheme(CurrAppTheme),
                        shadowColor = uiState.categoryColor
                            .getCategoryIconSolidColorByTheme(CurrAppTheme)
                    )
                }
            }
        }
    }
}



@Preview
@Composable
private fun CategoryStatisticsItemComponentPreview() {
    CategoryStatisticsScreenPreview()
}
