package com.ataglance.walletglance.core.presentation.component.button

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect

@Composable
fun SmallFilledIconButton(
    iconRes: Int,
    iconContendDescription: String,
    containerColor: Color = GlanciColors.surface,
    contentColor: Color = GlanciColors.onSurface,
    size: Dp = 24.dp,
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
            modifier = Modifier.size(size)
        )
    }
}