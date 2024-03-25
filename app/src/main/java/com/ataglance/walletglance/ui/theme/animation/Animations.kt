package com.ataglance.walletglance.ui.theme.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

sealed interface Animations {

    val openWindowAnimation: EnterTransition
        get() = scaleIn(
            animationSpec = tween(
                durationMillis = 350,
                delayMillis = 50,
                easing = EaseOutBack
            ),
            initialScale = .8f
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = 250,
                easing = EaseInOut
            ),
            initialAlpha = 0f
        )

    val closeWindowAnimation: ExitTransition
        get() = scaleOut(
            animationSpec = tween(
                durationMillis = 400,
                easing = EaseInOut
            ),
            targetScale = .5f
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = 300,
                delayMillis = 100,
                easing = EaseOutBack
            ),
            targetAlpha = 0f
        )
}