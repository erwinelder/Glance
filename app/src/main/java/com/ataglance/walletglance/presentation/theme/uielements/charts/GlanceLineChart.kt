package com.ataglance.walletglance.presentation.theme.uielements.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.presentation.theme.GlanceTheme
import com.ataglance.walletglance.presentation.theme.screens.BudgetsScreenPreview

@Composable
fun GlanceLineChart(
    filledWidth: Float,
    gradientBrushColors: List<Color>,
    shadowColor: Color
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
    ) {
        Spacer(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(GlanceTheme.glassGradientLightToDark.first)
                .fillMaxWidth()
                .height(16.dp)
        )
        Spacer(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    spotColor = shadowColor,
                    shape = RoundedCornerShape(50)
                )
                .clip(RoundedCornerShape(50))
                .background(brush = Brush.linearGradient(gradientBrushColors))
                .fillMaxWidth(filledWidth)
                .height(16.dp)
        )
    }
}


@Preview
@Composable
private fun Preview() {
    BudgetsScreenPreview()
}