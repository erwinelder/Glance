package com.ataglance.walletglance.core.presentation.component.container

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    filledWidths: FilledWidthByScreenType? = FilledWidthByScreenType(),
    cornerSize: Dp = dimensionResource(R.dimen.widget_corner_size),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .run {
                filledWidths?.let { fillMaxWidth(it.getByType(CurrWindowType)) } ?: this
            }
            .clip(RoundedCornerShape(cornerSize))
            .background(
                brush = Brush.linearGradient(
                    colors = GlanceColors.glassGradient,
                    start = Offset(0f, 1400f),
                    end = Offset(600f, 0f)
                )
            )
            .border(
                width = 2.dp,
                color = GlanceColors.glassBorder,
                shape = RoundedCornerShape(cornerSize)
            )
            .padding(2.dp)
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