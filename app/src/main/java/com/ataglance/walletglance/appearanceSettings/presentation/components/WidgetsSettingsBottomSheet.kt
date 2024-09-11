package com.ataglance.walletglance.appearanceSettings.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import com.ataglance.walletglance.appearanceSettings.domain.model.CheckedWidget
import com.ataglance.walletglance.appearanceSettings.domain.model.WidgetName
import com.ataglance.walletglance.core.presentation.components.containers.GlanceBottomSheet
import com.ataglance.walletglance.core.presentation.components.containers.ReorderableListStyled

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetsSettingsBottomSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    widgetList: List<CheckedWidget>,
    onWidgetCheckedStateChange: (WidgetName, Boolean) -> Unit,
    onMoveWidgets: (Int, Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    GlanceBottomSheet(
        visible = visible,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        ReorderableListStyled(
            list = widgetList,
            onMoveItems = onMoveWidgets
        ) { item ->
            CheckedWidgetComponent(
                widget = item,
                onCheckedStateChange = onWidgetCheckedStateChange
            )
        }
    }
}