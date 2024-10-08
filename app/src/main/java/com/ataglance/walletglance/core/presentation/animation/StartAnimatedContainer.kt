package com.ataglance.walletglance.core.presentation.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable

@Composable
fun StartAnimatedContainer(
    visible: Boolean,
    delayMillis: Int = 0,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = widgetEnterTransition(delayMillis),
    ) {
        content()
    }
}