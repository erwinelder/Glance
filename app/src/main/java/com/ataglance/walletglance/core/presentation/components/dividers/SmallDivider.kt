package com.ataglance.walletglance.core.presentation.components.dividers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.GlanceTheme

@Composable
fun SmallDivider(
    modifier: Modifier = Modifier,
    filledWidth: Float = .3f,
    thickness: Dp = 1.dp,
    color: Color = GlanceTheme.outline.copy(alpha = .5f)
) {
    HorizontalDivider(
        modifier = modifier
            .fillMaxWidth(filledWidth)
            .clip(RoundedCornerShape(1.dp)),
        thickness = thickness,
        color = color
    )
}