package com.ataglance.walletglance.core.presentation.modifier

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import com.ataglance.walletglance.core.presentation.vibration.Vibrator
import org.koin.compose.koinInject

fun Modifier.singleTapLightVibration(enabled: Boolean = true) = composed {
    val vibrator = koinInject<Vibrator>()

    var isPressed by remember { mutableStateOf(false) }

    this.pointerInput(isPressed) {
        awaitPointerEventScope {
            if (enabled) {
                isPressed = if (isPressed) {
                    waitForUpOrCancellation()
                    false
                } else {
                    awaitFirstDown(false)
                    vibrator.vibrateLightSingleTap()
                    true
                }
            }
        }
    }
}