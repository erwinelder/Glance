package com.ataglance.walletglance.ui.theme.uielements.pickers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.ui.theme.GlanceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    openDialog: Boolean,
    onOpenDateDialogChange: (Boolean) -> Unit,
    onConfirmButton: () -> Unit,
    state: DatePickerState
) {
    AnimatedVisibility(visible = openDialog) {
        DatePickerDialog(
            onDismissRequest = { onOpenDateDialogChange(false) },
            confirmButton = {
                TextButton(onClick = onConfirmButton) {
                    Text(text = stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onOpenDateDialogChange(false) }
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(
                state = state,
                colors = DatePickerDefaults.colors(
                    containerColor = GlanceTheme.surface,
                    titleContentColor = GlanceTheme.onSurface,
                    headlineContentColor = GlanceTheme.onSurface,
                    weekdayContentColor = GlanceTheme.onSurface,
                    subheadContentColor = GlanceTheme.onSurface,
                    yearContentColor = GlanceTheme.onSurface
                )
            )
        }
    }
}