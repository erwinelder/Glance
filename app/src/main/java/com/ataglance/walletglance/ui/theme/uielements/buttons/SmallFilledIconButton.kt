package com.ataglance.walletglance.ui.theme.uielements.buttons

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect

@Composable
fun SmallFilledIconButton(
    iconRes: Int,
    iconContendDescription: String,
    containerColor: Color = GlanceTheme.glassGradientLightToDark.first,
    contentColor: Color = GlanceTheme.onSurface,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    FilledIconButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(13.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = Modifier.bounceClickEffect(.96f)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = iconContendDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun BoxScope.SmallFilledIconButton(
    iconRes: Int,
    iconContendDescription: String,
    containerColor: Color = GlanceTheme.surface,
    contentColor: Color = GlanceTheme.onSurface,
    alignment: Alignment = Alignment.Center,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    FilledIconButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(13.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = Modifier
            .bounceClickEffect(.96f)
            .align(alignment)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = iconContendDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}