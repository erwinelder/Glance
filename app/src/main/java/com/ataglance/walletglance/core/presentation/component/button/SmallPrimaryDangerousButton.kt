package com.ataglance.walletglance.core.presentation.component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@Composable
fun SmallPrimaryDangerousButton(
    text: String,
    enabled: Boolean = true,
    fontSize: TextUnit = 17.sp,
    onClick: () -> Unit
) {
    SmallPrimaryButton(
        text = text,
        enabled = enabled,
        enabledGradient = GlanceColors.errorGradientPair,
        fontSize = fontSize,
        onClick = onClick
    )
}