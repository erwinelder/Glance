package com.ataglance.walletglance.core.presentation.modifiers

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
    onClick: () -> Unit = {}
) = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (enabled && isPressed) shrinkScale else 1f,
        label = "bounceClickEffect"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {
                if (enabled) {
                    onClick()
                }
            }
        )
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