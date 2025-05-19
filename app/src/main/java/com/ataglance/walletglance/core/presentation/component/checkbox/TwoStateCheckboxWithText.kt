package com.ataglance.walletglance.core.presentation.component.checkbox

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect

@Composable
fun TwoStateCheckboxWithText(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkboxSize: Dp = 28.dp,
    fontSize: TextUnit = 16.sp,
    onClick: (Boolean) -> Unit
) {
    TwoStateCheckboxContainer(
        checked = checked,
        modifier = modifier,
        enabled = enabled,
        checkboxSize = checkboxSize,
        onClick = onClick
    ) {
        Text(
            text = text,
            color = GlanceColors.onSurface,
            fontSize = fontSize,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.bounceClickEffect {
                onClick(!checked)
            }
        )
    }
}