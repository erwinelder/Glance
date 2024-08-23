package com.ataglance.walletglance.presentation.ui.uielements.switches

import androidx.compose.runtime.Composable
import com.ataglance.walletglance.presentation.ui.uielements.fields.FieldWithLabel

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