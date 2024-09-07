package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ataglance.walletglance.core.domain.componentState.DraggableItem
import kotlinx.coroutines.channels.Channel

@Composable
fun <T> ReorderableList(
    list: List<T>,
    onMoveItems: (Int, Int) -> Unit,
    horizontalContentPadding: Dp = 0.dp,
    verticalContentPadding: Dp = 16.dp,
    verticalGap: Dp = 8.dp,
    itemComponent: @Composable (T, Modifier) -> Unit
) {
    val lazyListState = rememberLazyListState()

    var draggingItemIndex by remember {
        mutableStateOf<Int?>(null)
    }
    var draggingItem by remember {
        mutableStateOf<LazyListItemInfo?>(null)
    }
    var delta by remember {
        mutableFloatStateOf(0f)
    }
    val scrollChannel = Channel<Float>()

    LaunchedEffect(lazyListState) {
        while (true) {
            val diff = scrollChannel.receive()
            lazyListState.scrollBy(diff)
        }
    }

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(
            horizontal = horizontalContentPadding, vertical = verticalContentPadding
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(verticalGap),
        modifier = Modifier
            .pointerInput(key1 = lazyListState) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { offset ->
                        lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { item ->
                            offset.y.toInt() in item.offset..(item.offset + item.size)
                        }?.also {
                            (it.contentType as? DraggableItem)?.let { draggableItem ->
                                draggingItem = it
                                draggingItemIndex = draggableItem.index
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        delta += dragAmount.y

                        val currentDraggingIndex = draggingItemIndex
                            ?: return@detectDragGesturesAfterLongPress
                        val currentDraggingItem = draggingItem
                            ?: return@detectDragGesturesAfterLongPress

                        val startOffset = currentDraggingItem.offset + delta
                        val endOffset = currentDraggingItem.offset + currentDraggingItem.size +
                                delta
                        val middleOffset = startOffset + (endOffset - startOffset) / 2

                        val targetItem = lazyListState.layoutInfo.visibleItemsInfo.find { item ->
                            middleOffset.toInt() in item.offset..(item.offset + item.size) &&
                                    currentDraggingItem.index != item.index &&
                                    item.contentType is DraggableItem
                        }

                        if (targetItem != null) {
                            val targetIndex = (targetItem.contentType as DraggableItem).index
                            onMoveItems(currentDraggingIndex, targetIndex)
                            draggingItemIndex = targetIndex
                            draggingItem = targetItem
                            delta += currentDraggingItem.offset - targetItem.offset
                        } else {
                            val startOffsetToTop = startOffset -
                                    lazyListState.layoutInfo.viewportStartOffset
                            val endOffsetBottom = endOffset -
                                    lazyListState.layoutInfo.viewportEndOffset
                            val scroll = when {
                                startOffsetToTop < 0 -> startOffsetToTop.coerceAtMost(0f)
                                endOffsetBottom > 0 -> endOffsetBottom.coerceAtMost(0f)
                                else -> 0f
                            }
                            val canScrollDown = currentDraggingIndex != list.size - 1 &&
                                    endOffsetBottom > 0
                            val canScrollUp = currentDraggingIndex != 0 && startOffsetToTop < 0
                            if (scroll != 0f && (canScrollUp || canScrollDown)) {
                                scrollChannel.trySend(scroll)
                            }
                        }
                    },
                    onDragEnd = {
                        draggingItem = null
                        draggingItemIndex = null
                        delta = 0f
                    },
                    onDragCancel = {
                        draggingItem = null
                        draggingItemIndex = null
                        delta = 0f
                    }
                )
            }
            .fillMaxWidth()
    ) {
        itemsIndexed(
            items = list,
            contentType = { index, _ -> DraggableItem(index = index) }
        ) { index, item ->
            val modifier = if (draggingItemIndex == index) {
                Modifier
                    .zIndex(1f)
                    .graphicsLayer { translationY = delta }
            } else {
                Modifier
            }

            itemComponent(item, modifier)
        }
    }
}