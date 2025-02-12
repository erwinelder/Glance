package com.ataglance.walletglance.core.presentation.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@Composable
fun SmallPrimaryDangerousButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fontSize: TextUnit = 17.sp,
    onClick: () -> Unit
) {
    SmallPrimaryButton(
        text = text,
        modifier = modifier,
        enabled = enabled,
        enabledGradient = GlanceColors.errorGradientPair,
        fontSize = fontSize,
        onClick = onClick
    )
}