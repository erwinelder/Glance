package com.ataglance.walletglance.core.presentation.component.switches

import androidx.compose.runtime.Composable
import com.ataglance.walletglance.core.presentation.component.fields.FieldWithLabel

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