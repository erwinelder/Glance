package com.ataglance.walletglance.core.presentation.components.switches

import androidx.compose.runtime.Composable
import com.ataglance.walletglance.core.presentation.components.fields.FieldWithLabel

@Composable
fun SwitchWithLabel(
    checked: Boolean,
    labelText: String = "Label",
    onCheckedChange: (Boolean) -> Unit
) {
    FieldWithLabel(labelText) {
        GlanceSwitch(checked = checked, onChecked = onCheckedChange)
    }
}