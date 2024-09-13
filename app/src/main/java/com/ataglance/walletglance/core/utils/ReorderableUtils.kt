package com.ataglance.walletglance.core.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import com.ataglance.walletglance.core.domain.componentState.DragDropState
import com.ataglance.walletglance.core.domain.componentState.DraggableItem

@Composable
fun rememberDragDropState(
    listState: LazyListState,
    listSize: Int,
    onMoveItems: (Int, Int) -> Unit
): DragDropState {
    val state = remember(listState) {
        DragDropState(
            listState = listState,
            listSize = listSize,
            onMoveItems = onMoveItems
        )
    }
    LaunchedEffect(state) {
        while (true) {
            val diff = state.scrollChannel.receive()
            if (diff != 0f) {
                listState.scrollBy(diff)
            }
        }
    }
    return state
}

inline fun <T> LazyListScope.draggableItems(
    dragDropState: DragDropState,
    items: List<T>,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    crossinline content: @Composable LazyItemScope.(T, Modifier) -> Unit,
) {
    itemsIndexed(
        items = items,
        contentType = { index, _ -> DraggableItem(index = index) },
        key = key
    ) { index, item ->
        val scale by animateFloatAsState(
            targetValue = if (dragDropState.draggingItemIndex == index) 1.05f else 1f,
            label = "reorderable item scale"
        )

        val modifier = if (dragDropState.draggingItemIndex == index) {
            Modifier
                .zIndex(1f)
                .graphicsLayer {
                    translationY = dragDropState.delta
                }
        } else {
            Modifier.animateItem()
        }
            .scale(scale)

        content(item, modifier)
    }
}
