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
import com.ataglance.walletglance.core.presentation.modifier.innerVolumeShadow
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.GlanciColors

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    filledWidths: FilledWidthByScreenType? = FilledWidthByScreenType(),
    cornerSize: Dp = dimensionResource(R.dimen.widget_corner_size),
    contentPadding: PaddingValues = PaddingValues(),
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
                    colors = GlanciColors.glassGradient,
                    start = Offset(1100f, 0f),
                    end = Offset(0f, 1200f)
                )
            )
            .innerVolumeShadow(
                shape = RoundedCornerShape(cornerSize),
                offset = 2.dp,
                blur = 2.dp,
                spread = 1.dp,
                whiteColor = GlanciColors.glassShadow.first,
                blackColor = GlanciColors.glassShadow.second
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = GlanciColors.glassBorderGradient,
                    start = Offset(100f, 0f),
                    end = Offset(0f, 120f)
                ),
                shape = RoundedCornerShape(cornerSize)
            )
            .padding(contentPadding)
            .padding(1.dp)
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