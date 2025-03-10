package com.ataglance.walletglance.core.presentation.components.pickers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlanceDateRangePicker(
    visible: Boolean,
    dateRangePickerState: DateRangePickerState,
    onCloseDialog: () -> Unit
) {
    AnimatedVisibility(visible = visible) {
        DatePickerDialog(
            onDismissRequest = onCloseDialog,
            confirmButton = {
                TextButton(onClick = onCloseDialog) {
                    Text(text = stringResource(R.string.close))
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = GlanceColors.surface,
                    titleContentColor = GlanceColors.onSurface,
                    headlineContentColor = GlanceColors.onSurface,
                    weekdayContentColor = GlanceColors.onSurface,
                    subheadContentColor = GlanceColors.onSurface,
                    yearContentColor = GlanceColors.onSurface
                )
            )
        }
    }
}