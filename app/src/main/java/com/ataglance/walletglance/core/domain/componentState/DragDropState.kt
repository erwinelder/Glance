package com.ataglance.walletglance.core.domain.componentState

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.channels.Channel

class DragDropState(
    private val listState: LazyListState,
    private val listSize: Int,
    private val onMoveItems: (Int, Int) -> Unit
) {

    private var draggingItem by mutableStateOf<LazyListItemInfo?>(null)
    var draggingItemIndex by mutableStateOf<Int?>(null)
    var delta by mutableFloatStateOf(0f)

    val scrollChannel = Channel<Float>()

    internal fun onDragStart(offset: Offset) {
        listState.layoutInfo.visibleItemsInfo
            .firstOrNull { item ->
                (offset.y.toInt() - listState.layoutInfo.afterContentPadding) in
                        item.offset..(item.offset + item.size)
            }?.also {
                (it.contentType as? DraggableItem)?.let { draggableItem ->
                    draggingItem = it
                    draggingItemIndex = draggableItem.index
                }
            }
    }

    internal fun onDrag(offset: Offset) {
        delta += offset.y

        val currDragIndex = draggingItemIndex ?: return
        val currDragItem = draggingItem ?: return

        val itemTopPos = currDragItem.offset + delta
        val itemBtmPos = currDragItem.offset + delta + currDragItem.size
        val itemMidPos = itemTopPos + (itemBtmPos - itemTopPos) / 2.0

        val targetItem = listState.layoutInfo.visibleItemsInfo.find { item ->
            itemMidPos.toInt() in item.offset..(item.offset + item.size) &&
                    currDragItem.index != item.index &&
                    item.contentType is DraggableItem
        }

        /*val startOffsetToTop = itemTopPos - listState.layoutInfo.viewportStartOffset - startPadding
        val endOffsetToBottom = itemBtmPos - listState.layoutInfo.viewportEndOffset + endPadding
        val scroll = when {
            startOffsetToTop < 0 -> startOffsetToTop.coerceAtMost(0f)
            endOffsetToBottom > 0 -> endOffsetToBottom.coerceAtLeast(0f)
            else -> 0f
        }

        if (scroll != 0f && currDragIndex != 0 && currDragIndex != listSize - 1) {
            val finalScroll = scroll * 0.25f
            scrollChannel.trySend(finalScroll)
        }*/

        if (targetItem != null) {
            val targetIndex = (targetItem.contentType as DraggableItem).index
            /*if (targetIndex == 0) {
                scrollChannel.trySend(targetItem.size.toFloat())
            }*/
            onMoveItems(currDragIndex, targetIndex)

            draggingItemIndex = targetIndex
            delta += currDragItem.offset - targetItem.offset
            draggingItem = targetItem
        }
    }

    internal fun onDragInterrupted() {
        draggingItem = null
        draggingItemIndex = null
        delta = 0f
    }

}