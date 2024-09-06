package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.components.dividers.SmallDivider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlanceBottomSheet(
    visible: Boolean,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    backgroundColor: Color = GlanceTheme.background,
    contentColor: Color = GlanceTheme.onBackground,
    content: @Composable () -> Unit
) {
    if (visible) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            shape = RoundedCornerShape(dimensionResource(R.dimen.widget_corner_size)),
            containerColor = backgroundColor,
            contentColor = contentColor,
            dragHandle = {
                SmallDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    thickness = 3.dp
                )
            }
        ) {
            content()
        }
    }
}