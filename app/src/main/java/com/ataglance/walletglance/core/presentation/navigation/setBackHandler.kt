package com.ataglance.walletglance.core.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun SetBackHandler(enabled: Boolean = true, onBack: () -> Unit = {}) {
    BackHandler(enabled = enabled, onBack = onBack)
}