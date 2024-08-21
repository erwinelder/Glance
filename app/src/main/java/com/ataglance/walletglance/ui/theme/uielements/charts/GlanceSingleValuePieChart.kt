package com.ataglance.walletglance.ui.theme.uielements.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlanceSingleValuePieChart(
    percentage: Float,
    brush: List<Color>,
    size: Dp
) {
    Canvas(
        modifier = Modifier
            .padding(end = 5.dp)
            .size(size)
            .rotate(-82f)
    ) {
        drawArc(
            brush = Brush.sweepGradient(brush),
            startAngle = -8f,
            sweepAngle = -percentage,
            useCenter = false,
            style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}