package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun GlassSurfaceOnGlassSurface(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enableClick: Boolean = true,
    filledWidth: Float? = null,
    paddingValues: PaddingValues = PaddingValues(16.dp, 8.dp),
    shrinkScale: Float = .98f,
    content: @Composable () -> Unit
) {
    var localModifier = modifier
        .bounceClickEffect(shrinkScale = shrinkScale, enabled = enableClick, onClick = onClick)
    localModifier = localModifier.clip(RoundedCornerShape(dimensionResource(R.dimen.record_corner_size)))
    if (filledWidth != null) {
        localModifier = localModifier.fillMaxWidth(filledWidth)
    }

    localModifier = localModifier
        .background(
            brush = Brush.linearGradient(
                colors = GlanceTheme.onGlassSurfaceGradient,
                start = Offset(75f, 200f),
                end = Offset(100f, 0f)
            )
        )
        .border(
            1.dp,
            GlanceTheme.onGlassSurfaceBorder,
            RoundedCornerShape(dimensionResource(R.dimen.record_corner_size))
        )
        .padding(paddingValues)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = localModifier
    ) {
        content()
    }
}