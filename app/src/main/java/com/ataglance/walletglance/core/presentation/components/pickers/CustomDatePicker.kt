package com.ataglance.walletglance.core.presentation.components.pickers

import androidx.compose.animation.AnimatedVisibility
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
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer

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
                    containerColor = GlanceColors.surface,
                    titleContentColor = GlanceColors.onSurface,
                    headlineContentColor = GlanceColors.onSurface,
                    weekdayContentColor = GlanceColors.onSurface,
                    subheadContentColor = GlanceColors.onSurface,
                    navigationContentColor = GlanceColors.onSurface,
                    yearContentColor = GlanceColors.onSurface,
                    disabledYearContentColor = GlanceColors.onSurface,
                    currentYearContentColor = GlanceColors.primary,
                    selectedYearContentColor = GlanceColors.onPrimary,
                    disabledSelectedYearContentColor = GlanceColors.onSurface,
                    selectedYearContainerColor = GlanceColors.primary,
                    disabledSelectedYearContainerColor = GlanceColors.onSurface,
                    dayContentColor = GlanceColors.onSurface,
                    disabledDayContentColor = GlanceColors.onSurface,
                    selectedDayContentColor = GlanceColors.onPrimary,
                    disabledSelectedDayContentColor = GlanceColors.onSurface,
                    selectedDayContainerColor = GlanceColors.primary,
                    disabledSelectedDayContainerColor = GlanceColors.onSurface,
                    todayContentColor = GlanceColors.primary,
                    todayDateBorderColor = GlanceColors.primary,
                    dayInSelectionRangeContentColor = GlanceColors.onSurface,
                    dayInSelectionRangeContainerColor = GlanceColors.onSurface,
                    dividerColor = GlanceColors.outline,
                    dateTextFieldColors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = GlanceColors.onSurface,
                        unfocusedIndicatorColor = GlanceColors.outline,
                        focusedLabelColor = GlanceColors.onSurface,
                        unfocusedLabelColor = GlanceColors.onSurface,
                        disabledLabelColor = GlanceColors.onSurface
                    )
                )
            )
        }
    }
}

@Preview
@Composable
private fun CustomDatePickerPreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        CustomDatePicker(
            openDialog = true,
            initialTimeInMillis = System.currentTimeMillis(),
            onOpenDateDialogChange = {},
            onConfirmButton = {}
        )
    }
}