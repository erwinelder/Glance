package com.ataglance.walletglance.core.presentation.component.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.budget.presentation.screen.BudgetsScreenPreview
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@Composable
fun GlanceLineChart(
    percentage: Float,
    brushColors: List<Color>,
    shadowColor: Color,
    height: Dp = 12.dp
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Spacer(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(GlanceColors.glassButtonGradientPair.first)
                .fillMaxWidth()
                .fillMaxHeight()
        )
        Spacer(
            modifier = Modifier
                .shadow(
                    elevation = height / 2,
                    spotColor = shadowColor,
                    shape = RoundedCornerShape(50)
                )
                .clip(RoundedCornerShape(50))
                .background(brush = Brush.linearGradient(brushColors))
                .fillMaxWidth(percentage)
                .fillMaxHeight()
        )
    }
}


@Preview
@Composable
private fun Preview() {
    BudgetsScreenPreview()
}