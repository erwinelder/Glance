package com.ataglance.walletglance.core.presentation.component.button

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

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
            color = GlanciColors.primary,
            fontSize = fontSize,
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal
        )
    }
}