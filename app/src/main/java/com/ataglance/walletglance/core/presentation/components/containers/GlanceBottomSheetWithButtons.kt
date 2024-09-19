package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryTextButton
import com.ataglance.walletglance.core.presentation.components.dividers.BottomSheetHandle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlanceBottomSheetWithButtons(
    visible: Boolean,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onSaveButtonClick: () -> Unit,
    backgroundColor: Color = GlanceTheme.background,
    contentColor: Color = GlanceTheme.onBackground,
    content: @Composable () -> Unit
) {
    GlanceBottomSheet(
        visible = visible,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        dragHandle = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PrimaryTextButton(
                        text = stringResource(R.string.cancel),
                        onClick = onDismissRequest
                    )
                    PrimaryTextButton(
                        text = stringResource(R.string.save),
                        onClick = onSaveButtonClick
                    )
                }
                BottomSheetHandle()
            }
        },
        content = content
    )
}