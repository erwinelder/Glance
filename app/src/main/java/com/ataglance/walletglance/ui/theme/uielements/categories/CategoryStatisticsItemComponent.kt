package com.ataglance.walletglance.ui.theme.uielements.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categories.CategoryStatisticsElementUiState
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurfaceOnGlassSurface

@Composable
fun CategoryStatisticsItemComponent(
    uiState: CategoryStatisticsElementUiState?,
    appTheme: AppTheme?,
    showLeftArrow: Boolean = false,
    enableClick: Boolean? = null,
    onClick: () -> Unit = {}
) {
    GlassSurfaceOnGlassSurface(
        modifier = Modifier.alpha(if (uiState == null) 0f else 1f),
        onClick = onClick,
        enableClick = enableClick == null &&
                uiState != null && (
                    uiState.subcategoriesStatisticsUiState != null || showLeftArrow
                ) || enableClick == true,
        filledWidth = 1f,
        paddingValues = PaddingValues(24.dp, 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
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
                                spotColor = uiState.categoryColor.getByTheme(appTheme).darker,
                                shape = RoundedCornerShape(30)
                            )
                            .clip(RoundedCornerShape(30))
                            .background(uiState.categoryColor.getByTheme(appTheme).darker)
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
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = uiState?.getTotalAmountWithCurrency() ?: "---",
                    color = GlanceTheme.onSurface,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = uiState?.getFormattedPercentage() ?: "---",
                    color = GlanceTheme.onSurface,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            uiState?.let { GlanceTheme.glassGradientLightToDark.first }
                                ?: Color.Transparent
                        )
                        .fillMaxWidth()
                        .height(16.dp)
                )
                uiState?.let {
                    Spacer(
                        modifier = Modifier
                            .shadow(
                                elevation = 8.dp,
                                spotColor = uiState.categoryColor.getByTheme(appTheme).darker,
                                shape = RoundedCornerShape(50)
                            )
                            .clip(RoundedCornerShape(50))
                            .background(
                                brush = Brush.linearGradient(
                                    uiState.categoryColor.getByTheme(appTheme).asListDarkToLight()
                                )
                            )
                            .fillMaxWidth(uiState.percentage / 100f)
                            .height(16.dp)
                    )
                }
            }
        }
    }
}