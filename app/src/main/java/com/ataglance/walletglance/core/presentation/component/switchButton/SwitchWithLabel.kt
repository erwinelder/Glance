package com.ataglance.walletglance.core.presentation.component.switchButton

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.component.field.FieldWithLabelWrapper

@Composable
fun SwitchWithLabel(
    checked: Boolean,
    labelText: String,
    onCheckedChange: (Boolean) -> Unit
) {
    FieldWithLabelWrapper(labelText = labelText, gap = 8.dp) {
        SwitchButton(checked = checked, onChecked = onCheckedChange)
    }
}