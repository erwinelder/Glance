package com.ataglance.walletglance.core.presentation.components.other

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.GlanceTheme

@Composable
fun IconWithBackground(
    @DrawableRes iconRes: Int,
    backgroundGradient: List<Color>,
    iconDescription: String,
    iconTint: Color = GlanceTheme.background,
    iconSize: Dp = 120.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                brush = Brush.linearGradient(
                    colors = backgroundGradient,
                    start = Offset(240f, 0f),
                    end = Offset(0f, 240f)
                )
            )
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = iconDescription,
            tint = iconTint,
            modifier = Modifier.size(iconSize)
        )
    }
}
