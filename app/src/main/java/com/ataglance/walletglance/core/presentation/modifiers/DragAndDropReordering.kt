package com.ataglance.walletglance.core.presentation.modifiers

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.ataglance.walletglance.core.domain.componentState.DragDropState

fun Modifier.dragAndDropReordering(dragDropState: DragDropState): Modifier {
    return this.pointerInput(dragDropState) {
        detectDragGesturesAfterLongPress(
            onDrag = { change, offset ->
                change.consume()
                dragDropState.onDrag(offset = offset)
            },
            onDragStart = dragDropState::onDragStart,
            onDragEnd = dragDropState::onDragInterrupted,
            onDragCancel = dragDropState::onDragInterrupted
        )
    }
}