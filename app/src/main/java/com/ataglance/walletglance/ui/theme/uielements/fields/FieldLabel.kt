package com.ataglance.walletglance.ui.theme.uielements.fields

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.Manrope

@Composable
fun FieldLabel(
    text: String,
    fontSize: TextUnit = 16.sp
) {
    Text(
        text = text,
        fontSize = fontSize,
        color = GlanceTheme.outline,
        fontWeight = FontWeight.Normal,
        fontFamily = Manrope,
        textAlign = TextAlign.Center
    )
}