package com.ataglance.walletglance.core.presentation.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun GlanceFloatingButton(
    @DrawableRes iconRes: Int,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent,
            contentColor = GlanceColors.onSurface,
            disabledContentColor = Color.Transparent,
            disabledContainerColor = GlanceColors.onSurface
        ),
        modifier = Modifier
            .bounceClickEffect()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(38),
                spotColor = GlanceColors.primary
            )
            .clip(RoundedCornerShape(38))
            .background(
                brush = Brush.linearGradient(
                    colors = GlanceColors.primaryGradient.reversed(),
                    start = Offset(75f, 210f),
                    end = Offset(95f, -10f)
                )
            )
            .padding(6.dp)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = "make record",
            tint = GlanceColors.onPrimary,
            modifier = Modifier.size(24.dp)
        )
    }
}