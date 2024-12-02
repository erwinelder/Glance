package com.ataglance.walletglance.core.presentation.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.GlanceTheme

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
        enabledGradientColor = GlanceTheme.errorGradientLightToDark,
        fontSize = fontSize,
        onClick = onClick
    )
}