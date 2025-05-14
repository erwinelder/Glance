package com.ataglance.walletglance.core.presentation.component.pickers

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
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePicker(
    openDialog: Boolean,
    initialHourAndMinute: Pair<Int, Int>,
    onOpenDialogChange: (Boolean) -> Unit,
    onConfirmButton: (Int, Int) -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = initialHourAndMinute.first,
        initialMinute = initialHourAndMinute.second,
        is24Hour = true
    )

    AnimatedVisibility(visible = openDialog) {
        DatePickerDialog(
            onDismissRequest = { onOpenDialogChange(false) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmButton(state.hour, state.minute)
                    }
                ) {
                    Text(text = stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onOpenDialogChange(false) }
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
                    containerColor = GlanceColors.surface,
                    timeSelectorSelectedContainerColor = GlanceColors.primary,
                    timeSelectorSelectedContentColor = GlanceColors.onPrimary
                ),
                layoutType = TimePickerLayoutType.Vertical
            )
        }
    }
}