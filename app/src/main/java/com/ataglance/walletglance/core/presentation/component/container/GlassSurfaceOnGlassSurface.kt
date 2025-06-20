package com.ataglance.walletglance.core.presentation.component.container

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.GlanciColors

@Composable
fun GlassSurfaceOnGlassSurface(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    clickEnabled: Boolean = true,
    shrinkScale: Float = .98f,
    filledWidth: Float? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(4.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = verticalArrangement,
        modifier = modifier
            .bounceClickEffect(shrinkScale = shrinkScale, enabled = clickEnabled, onClick = onClick)
            .clip(RoundedCornerShape(26.dp))
            .run {
                filledWidth?.let { fillMaxWidth(it) } ?: this
            }
            .background(
                brush = Brush.linearGradient(
                    colors = GlanciColors.glassGradientOnGlass,
                    start = Offset(75f, 200f),
                    end = Offset(100f, 0f)
                )
            )
            .border(
                width = 1.dp,
                color = GlanciColors.glassGradientOnGlassBorder,
                shape = RoundedCornerShape(26.dp)
            )
            .padding(contentPadding)
    ) {
        content()
    }
}