package com.ataglance.walletglance.core.presentation.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection


@Composable
fun isKeyboardVisible(): State<Boolean> {
    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isKeyboardVisible)
}

@Composable
fun getImeBottomInset(): State<Dp> {
    val imeBottomInset = WindowInsets.ime.getBottom(LocalDensity.current)
    val imeBottomInsetDp = with(LocalDensity.current) { imeBottomInset.toDp() }
    return rememberUpdatedState(imeBottomInsetDp)
}


fun PaddingValues.add(padding: PaddingValues): PaddingValues {
    return PaddingValues(
        start = this.calculateStartPadding(LayoutDirection.Ltr) +
                padding.calculateStartPadding(LayoutDirection.Ltr),
        top = this.calculateTopPadding() + padding.calculateTopPadding(),
        end = this.calculateEndPadding(LayoutDirection.Ltr) +
                padding.calculateEndPadding(LayoutDirection.Ltr),
        bottom = this.calculateBottomPadding() + padding.calculateBottomPadding()
    )
}