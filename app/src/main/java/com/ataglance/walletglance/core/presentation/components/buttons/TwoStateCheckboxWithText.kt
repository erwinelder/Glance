package com.ataglance.walletglance.core.presentation.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun TwoStateCheckboxWithText(
    text: String,
    checked: Boolean,
    enabled: Boolean = true,
    size: Dp = 28.dp,
    onClick: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TwoStateCheckbox(checked = checked, enabled = enabled, size = size, onClick = onClick)
        Text(
            text = text,
            color = GlanceTheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .bounceClickEffect {
                    onClick(!checked)
                }
        )
    }
}