package com.ataglance.walletglance.core.presentation.component.container.reorderable

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T> ReorderableListStyled(
    list: List<T>,
    itemKey: ((index: Int, item: T) -> Any)? = null,
    onMoveItems: (Int, Int) -> Unit,
    horizontalContentPadding: Dp = 24.dp,
    verticalContentPadding: Dp = 16.dp,
    verticalGap: Dp = 16.dp,
    itemContent: @Composable RowScope.(T) -> Unit
) {
    ReorderableList(
        list = list,
        onMoveItems = onMoveItems,
        horizontalContentPadding = horizontalContentPadding,
        verticalContentPadding = verticalContentPadding,
        horizontalAlignment = Alignment.Start,
        verticalGap = verticalGap,
        itemKey = itemKey
    ) { item, modifier ->
        ReorderableListItem(modifier = modifier) {
            itemContent(item)
        }
    }
}