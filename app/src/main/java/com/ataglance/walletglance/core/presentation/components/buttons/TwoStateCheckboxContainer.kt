package com.ataglance.walletglance.core.presentation.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TwoStateCheckboxContainer(
    checked: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkboxSize: Dp = 28.dp,
    onClick: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        TwoStateCheckbox(
            checked = checked,
            enabled = enabled,
            size = checkboxSize,
            onClick = onClick
        )
        content()
    }
}