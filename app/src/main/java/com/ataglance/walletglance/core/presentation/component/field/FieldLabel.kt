package com.ataglance.walletglance.core.presentation.component.field

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun FieldLabel(
    text: String,
    fontSize: TextUnit = 16.sp
) {
    Text(
        text = text,
        color = GlanceColors.outline,
        fontSize = fontSize,
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center
    )
}