package com.ataglance.walletglance.core.presentation.component.bottomSheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.component.divider.BottomSheetHandle
import com.ataglance.walletglance.core.presentation.theme.GlanciColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetComponent(
    visible: Boolean,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    backgroundColor: Color = GlanciColors.background,
    contentColor: Color = GlanciColors.onSurface,
    dragHandle: @Composable () -> Unit = {
        BottomSheetHandle()
    },
    content: @Composable ColumnScope.() -> Unit
) {
    if (visible) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            shape = RoundedCornerShape(
                topStart = dimensionResource(R.dimen.widget_corner_size),
                topEnd = dimensionResource(R.dimen.widget_corner_size)
            ),
            containerColor = backgroundColor,
            contentColor = contentColor,
            dragHandle = dragHandle
        ) {
            content()
        }
    }
}