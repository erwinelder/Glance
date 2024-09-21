package com.ataglance.walletglance.core.presentation.animation

import androidx.compose.runtime.Composable

@Composable
fun WidgetStartAnimatedContainer(
    visible: Boolean,
    index: Int,
    content: @Composable () -> Unit
) {
    StartAnimatedContainer(visible = visible, delayMillis = (index + 3) * 50, content = content)
}