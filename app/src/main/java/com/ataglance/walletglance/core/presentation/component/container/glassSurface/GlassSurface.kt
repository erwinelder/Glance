package com.ataglance.walletglance.core.presentation.component.container.glassSurface

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.GlanciColors

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    filledWidths: FilledWidthByScreenType? = FilledWidthByScreenType(),
    gradientColor: List<Color> = GlanciColors.glassGradient,
    cornerSize: Dp = 38.dp,
    borderSize: Dp = 1.dp,
    contentPadding: PaddingValues = PaddingValues(),
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        contentAlignment = contentAlignment,
        modifier = modifier
            .run {
                filledWidths?.let { fillMaxWidth(it.getByType(CurrWindowType)) } ?: this
            }
            .clip(RoundedCornerShape(cornerSize))
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColor,
                    start = Offset(1100f, 0f),
                    end = Offset(0f, 1200f)
                )
            )
            .border(
                width = borderSize,
                brush = Brush.linearGradient(
                    colors = GlanciColors.glassBorderGradient,
                    start = Offset(100f, 0f),
                    end = Offset(0f, 120f)
                ),
                shape = RoundedCornerShape(cornerSize)
            )
            .padding(borderSize)
            .padding(contentPadding)
    ) {
        content()
    }
}

@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun GlassSurfacePreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        GlassSurface {
            Box(modifier = Modifier.size(300.dp))
        }
    }
}