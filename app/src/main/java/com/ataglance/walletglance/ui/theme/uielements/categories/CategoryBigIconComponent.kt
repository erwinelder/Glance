package com.ataglance.walletglance.ui.theme.uielements.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.modifiers.innerShadow
import com.ataglance.walletglance.ui.theme.screens.BudgetStatisticsScreenPreview

@Composable
fun CategoryBigIconComponent(category: Category, appTheme: AppTheme?) {
    Icon(
        painter = painterResource(category.icon.res),
        contentDescription = "category ${category.name} icon",
        tint = GlanceTheme.surface,
        modifier = Modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(30),
                spotColor = category.getColorByTheme(appTheme).lighter
            )
            .innerShadow(
                shape = RoundedCornerShape(30),
                color = Color.Black.copy(.1f),
                offsetX = 1.dp,
                offsetY = (-1).dp,
                blur = 4.dp,
                spread = 0.dp
            )
            .innerShadow(
                shape = RoundedCornerShape(30),
                color = Color.White.copy(.2f),
                offsetX = (-1).dp,
                offsetY = 1.dp,
                blur = 4.dp,
                spread = 0.dp
            )
            .clip(RoundedCornerShape(30))
            .background(brush = Brush.linearGradient(
                colors = category.getColorByTheme(appTheme).asListDarkToLight(),
                start = Offset(0f, 140f),
                end = Offset(140f, 00f)
            ))
            .size(68.dp)
            .padding(12.dp)
    )
}


@Preview
@Composable
private fun CategoryBigIconComponentPreview() {
    BudgetStatisticsScreenPreview()
}