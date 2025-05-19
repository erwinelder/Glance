package com.ataglance.walletglance.core.presentation.component.field

import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@Composable
fun TextSelectionColorsProviderWrapper(
    content: @Composable () -> Unit
) {
    val textSelectionColors = TextSelectionColors(
        handleColor = GlanceColors.primary,
        backgroundColor = GlanceColors.primary.copy(.5f),
    )

    CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
        content()
    }
}