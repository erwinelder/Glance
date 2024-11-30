package com.ataglance.walletglance.core.presentation.components.containers

import androidx.annotation.DrawableRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.core.presentation.GlanceTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlanceBottomSheetDialog(
    visible: Boolean,
    @DrawableRes iconRes: Int,
    iconDescription: String,
    iconGradientColor: List<Color> = GlanceTheme.primaryGradientLightToDark.toList(),
    title: String,
    titleColor: Color = GlanceTheme.onBackground,
    message: String,
    onDismissRequest: () -> Unit,
    bottomBlock: @Composable ((onSheetHide: () -> Unit) -> Unit)? = null
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    GlanceBottomSheet(
        visible = visible,
        sheetState = sheetState,
        onDismissRequest = {
            coroutineScope.launch { sheetState.hide() }
            onDismissRequest()
        },
        dragHandle = {}
    ) {
        GlanceBottomSheetContentDialog(
            iconRes = iconRes,
            iconDescription = iconDescription,
            iconGradientColor = iconGradientColor,
            title = title,
            titleColor = titleColor,
            message = message,
            bottomBlock = if (bottomBlock != null) {{
                bottomBlock {
                    coroutineScope.launch { sheetState.hide() }
                }
            }} else null
        )
    }
}