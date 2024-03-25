package com.ataglance.walletglance.ui.theme.uielements.switches

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.ui.theme.uielements.fields.FieldLabel

@Composable
fun SwitchBlock(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    labelText: String = "Label",
    labelFontSize: TextUnit = 16.sp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FieldLabel(text = labelText, fontSize = labelFontSize)
        CustomSwitch(checked = checked, onChecked = onCheckedChange)
    }
}