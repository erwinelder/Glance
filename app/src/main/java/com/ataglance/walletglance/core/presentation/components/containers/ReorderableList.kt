package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.presentation.modifiers.dragAndDropReordering
import com.ataglance.walletglance.core.utils.draggableItems
import com.ataglance.walletglance.core.utils.rememberDragDropState

@Composable
fun <T> ReorderableList(
    list: List<T>,
    onMoveItems: (Int, Int) -> Unit,
    itemKey: ((index: Int, item: T) -> Any)? = null,
    horizontalContentPadding: Dp = 24.dp,
    verticalContentPadding: Dp = 16.dp,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalGap: Dp = 16.dp,
    itemComponent: @Composable LazyItemScope.(T, Modifier) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val dragDropState = rememberDragDropState(
        listState = lazyListState,
        listSize = list.size,
        onMoveItems = onMoveItems
    )

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(
            horizontal = horizontalContentPadding, vertical = verticalContentPadding
        ),
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.spacedBy(verticalGap),
        modifier = Modifier
            .dragAndDropReordering(dragDropState)
            .fillMaxWidth()
    ) {
        draggableItems(
            dragDropState = dragDropState,
            items = list,
            key = itemKey,
        ) { item, modifier ->
            itemComponent(item, modifier)
        }
    }
}
