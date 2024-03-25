package com.ataglance.walletglance.ui.theme.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SetupImportScreen(
    onNextNavigationButton: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Import data")
        Button(onClick = onNextNavigationButton) {
            Text(text = "Continue")
        }
    }
}