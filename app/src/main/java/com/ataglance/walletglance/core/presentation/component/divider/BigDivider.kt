package com.ataglance.walletglance.core.presentation.component.divider

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@Composable
fun BigDivider(
    modifier: Modifier = Modifier,
    filledWidth: Float = .75f,
    color: Color = GlanceColors.outline
) {
    HorizontalDivider(
        modifier = modifier
            .fillMaxWidth(filledWidth)
            .clip(RoundedCornerShape(1.dp)),
        thickness = 1.dp,
        color = color
    )
}