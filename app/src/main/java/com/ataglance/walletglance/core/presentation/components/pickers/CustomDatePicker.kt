package com.ataglance.walletglance.core.presentation.components.pickers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.WalletGlanceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    openDialog: Boolean,
    initialTimeInMillis: Long,
    onOpenDateDialogChange: (Boolean) -> Unit,
    onConfirmButton: (Long) -> Unit
) {
    val state = rememberDatePickerState(initialSelectedDateMillis = initialTimeInMillis)

    AnimatedVisibility(visible = openDialog) {
        DatePickerDialog(
            onDismissRequest = { onOpenDateDialogChange(false) },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.selectedDateMillis?.let { onConfirmButton(it) }
                    }
                ) {
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
                    navigationContentColor = GlanceTheme.onSurface,
                    yearContentColor = GlanceTheme.onSurface,
                    disabledYearContentColor = GlanceTheme.onSurface,
                    currentYearContentColor = GlanceTheme.primary,
                    selectedYearContentColor = GlanceTheme.onPrimary,
                    disabledSelectedYearContentColor = GlanceTheme.onSurface,
                    selectedYearContainerColor = GlanceTheme.primary,
                    disabledSelectedYearContainerColor = GlanceTheme.onSurface,
                    dayContentColor = GlanceTheme.onSurface,
                    disabledDayContentColor = GlanceTheme.onSurface,
                    selectedDayContentColor = GlanceTheme.onPrimary,
                    disabledSelectedDayContentColor = GlanceTheme.onSurface,
                    selectedDayContainerColor = GlanceTheme.primary,
                    disabledSelectedDayContainerColor = GlanceTheme.onSurface,
                    todayContentColor = GlanceTheme.primary,
                    todayDateBorderColor = GlanceTheme.primary,
                    dayInSelectionRangeContentColor = GlanceTheme.onSurface,
                    dayInSelectionRangeContainerColor = GlanceTheme.onSurface,
                    dividerColor = GlanceTheme.outline,
                    dateTextFieldColors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = GlanceTheme.onSurface,
                        unfocusedIndicatorColor = GlanceTheme.outline,
                        focusedLabelColor = GlanceTheme.onSurface,
                        unfocusedLabelColor = GlanceTheme.onSurface,
                        disabledLabelColor = GlanceTheme.onSurface
                    )
                )
            )
        }
    }
}

@Preview
@Composable
private fun CustomDatePickerPreview() {
    BoxWithConstraints {
        WalletGlanceTheme(
            boxWithConstraintsScope = this
        ) {
            CustomDatePicker(
                openDialog = true,
                initialTimeInMillis = System.currentTimeMillis(),
                onOpenDateDialogChange = {},
                onConfirmButton = {}
            )
        }
    }
}