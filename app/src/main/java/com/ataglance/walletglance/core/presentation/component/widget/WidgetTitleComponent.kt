package com.ataglance.walletglance.core.presentation.component.widget

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

@Composable
fun WidgetTitleComponent(
    title: String,
    lineHeight: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        color = GlanciColors.onSurface,
        fontSize = 24.sp,
        lineHeight = lineHeight,
        fontFamily = Manrope,
        fontWeight = FontWeight.W500,
        textAlign = textAlign,
        modifier = modifier
    )
}