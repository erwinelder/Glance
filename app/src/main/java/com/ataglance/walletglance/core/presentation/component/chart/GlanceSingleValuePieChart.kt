package com.ataglance.walletglance.core.presentation.component.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.component.container.GlassSurface
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer

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


@Preview
@Composable
private fun Preview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        GlassSurface {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
            ) {
                GlanceSingleValuePieChart(
                    percentage = 50f * 3.6f,
                    brush = GlanceColors.primaryGradient,
                    size = 90.dp
                )
            }
        }
    }
}