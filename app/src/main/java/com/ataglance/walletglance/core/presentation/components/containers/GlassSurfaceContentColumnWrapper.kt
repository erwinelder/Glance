package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GlassSurfaceContentColumnWrapper(
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    paddingValues: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = verticalArrangement,
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        content()
    }
}