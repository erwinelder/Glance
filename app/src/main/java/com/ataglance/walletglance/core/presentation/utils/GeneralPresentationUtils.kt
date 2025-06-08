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


operator fun PaddingValues.plus(paddingValues: PaddingValues): PaddingValues {
    return PaddingValues(
        start = this.start + paddingValues.start,
        top = this.top + paddingValues.top,
        end = this.end + paddingValues.end,
        bottom = this.bottom + paddingValues.bottom
    )
}

fun PaddingValues.plusBottomPadding(padding: Dp): PaddingValues {
    return PaddingValues(
        start = this.start,
        top = this.top,
        end = this.end,
        bottom = this.bottom + padding
    )
}

val PaddingValues.start: Dp
    get() = calculateStartPadding(LayoutDirection.Ltr)

val PaddingValues.end: Dp
    get() = calculateEndPadding(LayoutDirection.Ltr)

val PaddingValues.top: Dp
    get() = calculateTopPadding()

val PaddingValues.bottom: Dp
    get() = calculateBottomPadding()
