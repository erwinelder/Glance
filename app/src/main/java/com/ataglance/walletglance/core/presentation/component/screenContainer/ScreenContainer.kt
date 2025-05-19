package com.ataglance.walletglance.core.presentation.component.screenContainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.utils.add

@Composable
fun ScreenContainer(
    modifier: Modifier = Modifier,
    screenPadding: PaddingValues = PaddingValues(0.dp),
    padding: PaddingValues = PaddingValues(vertical = 24.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = verticalArrangement,
        modifier = modifier
            .padding(padding.add(screenPadding))
            .fillMaxSize()
    ) {
        content()
    }
}