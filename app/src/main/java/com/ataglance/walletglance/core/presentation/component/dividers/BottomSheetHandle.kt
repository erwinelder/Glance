package com.ataglance.walletglance.core.presentation.component.dividers

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetHandle() {
    SmallDivider(
        modifier = Modifier.padding(vertical = 16.dp),
        filledWidth = .2f,
        thickness = 3.dp
    )
}