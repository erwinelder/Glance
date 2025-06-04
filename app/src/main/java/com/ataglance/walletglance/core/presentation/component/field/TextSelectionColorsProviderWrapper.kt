package com.ataglance.walletglance.core.presentation.component.field

import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.ataglance.walletglance.core.presentation.theme.GlanciColors

@Composable
fun TextSelectionColorsProviderWrapper(
    content: @Composable () -> Unit
) {
    val textSelectionColors = TextSelectionColors(
        handleColor = GlanciColors.primary,
        backgroundColor = GlanciColors.primary.copy(.5f),
    )

    CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
        content()
    }
}