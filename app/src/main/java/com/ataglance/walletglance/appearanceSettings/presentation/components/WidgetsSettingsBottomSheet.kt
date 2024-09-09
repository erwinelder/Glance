package com.ataglance.walletglance.appearanceSettings.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.appearanceSettings.domain.model.WidgetName
import com.ataglance.walletglance.core.presentation.components.containers.GlanceBottomSheet
import com.ataglance.walletglance.core.presentation.components.containers.ReorderableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetsSettingsBottomSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    widgetNamesList: List<WidgetName>,
    onMoveWidgets: (Int, Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    GlanceBottomSheet(
        visible = visible,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        ReorderableList(
            list = widgetNamesList,
            onMoveItems = onMoveWidgets,
            verticalGap = 16.dp
        ) { item, modifier ->
            WidgetNameComponent(item, modifier)
        }
    }
}