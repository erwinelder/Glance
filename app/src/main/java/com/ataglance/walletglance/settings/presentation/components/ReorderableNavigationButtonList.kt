package com.ataglance.walletglance.settings.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ataglance.walletglance.core.presentation.components.containers.ReorderableList
import com.ataglance.walletglance.core.presentation.components.other.ScreenNameWithIconComponent
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton

@Composable
fun ReorderableNavigationButtonList() {
    var buttonList by remember {
        mutableStateOf(
            listOf(
                BottomBarNavigationButton.Records,
                BottomBarNavigationButton.CategoryStatistics,
                BottomBarNavigationButton.Budgets
            )
        )
    }
    val onMove = { fromIndex: Int, toIndex: Int ->
        buttonList = buttonList.toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
    }

    ReorderableList(
        list = buttonList,
        onSwapItems = onMove
    ) { item, modifier ->
        ScreenNameWithIconComponent(navigationButton = item, modifier = modifier)
    }
}
