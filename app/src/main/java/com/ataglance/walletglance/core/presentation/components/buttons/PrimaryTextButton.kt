package com.ataglance.walletglance.core.presentation.components.buttons

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.Manrope
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun PrimaryTextButton(
    text: String,
    fontSize: TextUnit = 18.sp,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.bounceClickEffect()
    ) {
        Text(
            text = text,
            color = GlanceColors.primary,
            fontSize = fontSize,
            fontWeight = FontWeight.Normal,
            fontFamily = Manrope
        )
    }
}