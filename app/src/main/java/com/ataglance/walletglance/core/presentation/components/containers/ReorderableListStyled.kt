package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T> ReorderableListStyled(
    list: List<T>,
    onMoveItems: (Int, Int) -> Unit,
    useOnlyWithLongPress: Boolean = true,
    horizontalContentPadding: Dp = 24.dp,
    verticalContentPadding: Dp = 16.dp,
    verticalGap: Dp = 16.dp,
    itemKey: ((index: Int, item: T) -> Any)? = null,
    itemContent: @Composable RowScope.(T) -> Unit
) {
    ReorderableList(
        list = list,
        onMoveItems = onMoveItems,
        useOnlyWithLongPress = useOnlyWithLongPress,
        horizontalContentPadding = horizontalContentPadding,
        verticalContentPadding = verticalContentPadding,
        verticalGap = verticalGap,
        itemKey = itemKey
    ) { item, modifier ->
        ReorderableListItem(modifier = modifier) {
            itemContent(item)
        }
    }
}