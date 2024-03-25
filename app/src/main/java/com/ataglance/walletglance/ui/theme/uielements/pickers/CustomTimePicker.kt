package com.ataglance.walletglance.ui.theme.uielements.pickers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.ui.theme.GlanceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePicker(
    openDialog: Boolean,
    onOpenTimeDialogChange: (Boolean) -> Unit,
    onConfirmButton: () -> Unit,
    state: TimePickerState
) {
    AnimatedVisibility(visible = openDialog) {
        DatePickerDialog(
            onDismissRequest = { onOpenTimeDialogChange(false) },
            confirmButton = {
                TextButton(onClick = onConfirmButton) {
                    Text(text = stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onOpenTimeDialogChange(false) }
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        ) {
            TimePicker(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                colors = TimePickerDefaults.colors(
                    containerColor = GlanceTheme.surface,
                    timeSelectorSelectedContainerColor = GlanceTheme.primary,
                    timeSelectorSelectedContentColor = GlanceTheme.onPrimary
                ),
                layoutType = TimePickerLayoutType.Vertical
            )
        }
    }
}