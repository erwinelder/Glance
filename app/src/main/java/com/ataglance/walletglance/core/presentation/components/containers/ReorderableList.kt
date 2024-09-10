package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.componentState.DragGesturesFunctions
import com.ataglance.walletglance.core.domain.componentState.DraggableItem
import com.ataglance.walletglance.core.presentation.GlanceTheme
import kotlinx.coroutines.channels.Channel

@Composable
fun <T> ReorderableList(
    list: List<T>,
    onMoveItems: (Int, Int) -> Unit,
    useOnlyWithLongPress: Boolean = true,
    horizontalContentPadding: Dp = 24.dp,
    verticalContentPadding: Dp = 16.dp,
    verticalGap: Dp = 8.dp,
    itemComponent: @Composable RowScope.(T) -> Unit
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

    val dragGesturesFunctions by remember {
        derivedStateOf {
            DragGesturesFunctions(
                onDragStart = { offset ->
                    onDragStart(
                        offset = offset,
                        lazyListState = lazyListState,
                        onSetDraggingItem = { draggingItem = it },
                        onSetDraggingItemIndex = { draggingItemIndex = it }
                    )
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
                },
                onDrag = { change, dragAmount ->
                    onDrag(
                        change = change,
                        dragAmount = dragAmount,
                        lazyListState = lazyListState,
                        listSize = list.size,
                        scrollChannel = scrollChannel,
                        delta = delta,
                        onSetDelta = { delta = it },
                        onMoveItems = onMoveItems,
                        draggingItem = draggingItem,
                        onSetDraggingItem = { draggingItem = it },
                        draggingItemIndex = draggingItemIndex,
                        onSetDraggingItemIndex = { draggingItemIndex = it }
                    )
                }
            )
        }
    }

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(
            horizontal = horizontalContentPadding, vertical = verticalContentPadding
        ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(verticalGap),
        modifier = Modifier
            .pointerInput(key1 = lazyListState) {
                detectDragGesturesAfterLongPress(
                    onDragStart = dragGesturesFunctions.onDragStart,
                    onDragEnd = dragGesturesFunctions.onDragEnd,
                    onDragCancel = dragGesturesFunctions.onDragCancel,
                    onDrag = dragGesturesFunctions.onDrag
                )
                if (!useOnlyWithLongPress) {
                    detectDragGestures(
                        onDragStart = dragGesturesFunctions.onDragStart,
                        onDragEnd = dragGesturesFunctions.onDragEnd,
                        onDragCancel = dragGesturesFunctions.onDragCancel,
                        onDrag = dragGesturesFunctions.onDrag
                    )
                }
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

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillParentMaxWidth(),
            ) {
                itemComponent(item)
                Icon(
                    painter = painterResource(R.drawable.reorder_icon),
                    contentDescription = "reorder",
                    tint = GlanceTheme.outline,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(start = 8.dp)
                )
            }
        }
    }
}

private fun onDragStart(
    offset: Offset,
    lazyListState: LazyListState,
    onSetDraggingItem: (LazyListItemInfo) -> Unit,
    onSetDraggingItemIndex: (Int) -> Unit
) {
    lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { item ->
        (offset.y.toInt() - lazyListState.layoutInfo.afterContentPadding) in
                item.offset..(item.offset + item.size)
    }?.also {
        (it.contentType as? DraggableItem)?.let { draggableItem ->
            onSetDraggingItem(it)
            onSetDraggingItemIndex(draggableItem.index)
        }
    }
}

private fun onDrag(
    change: PointerInputChange,
    dragAmount: Offset,
    lazyListState: LazyListState,
    listSize: Int,
    scrollChannel: Channel<Float>,
    delta: Float,
    onSetDelta: (Float) -> Unit,
    onMoveItems: (Int, Int) -> Unit,
    draggingItem: LazyListItemInfo?,
    onSetDraggingItem: (LazyListItemInfo) -> Unit,
    draggingItemIndex: Int?,
    onSetDraggingItemIndex: (Int) -> Unit,
) {
    change.consume()
    onSetDelta(delta + dragAmount.y)

    val currentDraggingIndex = draggingItemIndex ?: return
    val currentDraggingItem = draggingItem ?: return

    val startOffset = currentDraggingItem.offset + delta
    val endOffset = currentDraggingItem.offset + currentDraggingItem.size + delta
    val middleOffset = startOffset + (endOffset - startOffset) / 2

    val targetItem = lazyListState.layoutInfo.visibleItemsInfo.find { item ->
        middleOffset.toInt() in item.offset..(item.offset + item.size) &&
                currentDraggingItem.index != item.index &&
                item.contentType is DraggableItem
    }

    if (targetItem != null) {
        val targetIndex = (targetItem.contentType as DraggableItem).index
        onMoveItems(currentDraggingIndex, targetIndex)
        onSetDraggingItemIndex(targetIndex)
        onSetDraggingItem(targetItem)
        onSetDelta(delta + currentDraggingItem.offset - targetItem.offset)
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
        val canScrollDown = currentDraggingIndex != listSize - 1 && endOffsetBottom > 0
        val canScrollUp = currentDraggingIndex != 0 && startOffsetToTop < 0
        if (scroll != 0f && (canScrollUp || canScrollDown)) {
            scrollChannel.trySend(scroll)
        }
    }
}
