package com.ataglance.walletglance.core.domain.componentState

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange

data class DragGesturesFunctions(
    val onDragStart: (Offset) -> Unit,
    val onDragEnd: () -> Unit,
    val onDragCancel: () -> Unit,
    val onDrag: (PointerInputChange, Offset) -> Unit
)
