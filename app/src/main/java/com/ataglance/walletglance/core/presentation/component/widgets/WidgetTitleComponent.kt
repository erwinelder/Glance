package com.ataglance.walletglance.core.presentation.component.widgets

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@Composable
fun WidgetTitleComponent(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        color = GlanceColors.onSurface,
        fontSize = 24.sp,
        fontWeight = FontWeight.Light,
        modifier = modifier
    )
}