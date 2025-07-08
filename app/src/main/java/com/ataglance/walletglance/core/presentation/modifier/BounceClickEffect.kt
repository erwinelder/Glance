package com.ataglance.walletglance.core.presentation.modifier

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.bounceClickEffect(
    shrinkScale: Float = .97f,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    onClick: (() -> Unit)? = null
) = composed {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (enabled && isPressed) shrinkScale else 1f
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .run {
            onClick?.let {
                clickable(interactionSource = interactionSource, indication = null) {
                    if (enabled) onClick()
                }
            } ?: this
        }
        .pointerInput(isPressed) {
            awaitPointerEventScope {
                isPressed =
                    if (isPressed) {
                        waitForUpOrCancellation()
                        false
                    } else {
                        awaitFirstDown(false)
                        true
                    }
            }
        }
}