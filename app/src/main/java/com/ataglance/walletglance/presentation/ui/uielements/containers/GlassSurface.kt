package com.ataglance.walletglance.presentation.ui.uielements.containers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.presentation.ui.GlanceTheme
import com.ataglance.walletglance.presentation.ui.WindowTypeIsCompact
import com.ataglance.walletglance.presentation.ui.WindowTypeIsMedium
import com.ataglance.walletglance.presentation.ui.modifiers.innerShadow

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    filledWidth: Float? = null,
    cornerSize: Dp = dimensionResource(R.dimen.widget_corner_size),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth(
                filledWidth ?: when {
                    WindowTypeIsCompact -> .9f
                    WindowTypeIsMedium -> .67f
                    else -> .44f
                }
            )
            .clip(RoundedCornerShape(cornerSize))
            .background(
                brush = Brush.linearGradient(
                    colors = GlanceTheme.glassSurfaceGradient,
                    start = Offset(0f, 1400f),
                    end = Offset(600f, 0f)
                )
            )
            .innerShadow(
                shape = RoundedCornerShape(cornerSize),
                color = GlanceTheme.glassSurfaceLightAndDarkShadow.first,
                offsetX = -(5).dp,
                offsetY = 5.dp,
                blur = 13.dp,
                spread = 3.dp
            )
            .innerShadow(
                shape = RoundedCornerShape(cornerSize),
                color = GlanceTheme.glassSurfaceLightAndDarkShadow.second,
                offsetX = 5.dp,
                offsetY = -(5).dp,
                blur = 13.dp,
                spread = 3.dp
            )
            .border(
                2.dp,
                GlanceTheme.glassSurfaceBorder,
                RoundedCornerShape(cornerSize)
            )
    ) {
        content()
    }
}

@Preview(showSystemUi = true)
@Composable
private fun GlassSurfacePreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        GlassSurface {
            Box(modifier = Modifier.size(300.dp))
        }
    }
}