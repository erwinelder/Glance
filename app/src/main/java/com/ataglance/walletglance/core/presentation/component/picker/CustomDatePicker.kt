package com.ataglance.walletglance.core.presentation.component.picker

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
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope

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
                    Text(
                        text = stringResource(R.string.confirm),
                        fontFamily = Manrope
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onOpenDateDialogChange(false) }
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        fontFamily = Manrope
                    )
                }
            }
        ) {
            DatePicker(
                state = state,
                colors = DatePickerDefaults.colors(
                    containerColor = GlanciColors.surface,
                    titleContentColor = GlanciColors.onSurface,
                    headlineContentColor = GlanciColors.onSurface,
                    weekdayContentColor = GlanciColors.onSurface,
                    subheadContentColor = GlanciColors.onSurface,
                    navigationContentColor = GlanciColors.onSurface,
                    yearContentColor = GlanciColors.onSurface,
                    disabledYearContentColor = GlanciColors.onSurface,
                    currentYearContentColor = GlanciColors.primary,
                    selectedYearContentColor = GlanciColors.onPrimary,
                    disabledSelectedYearContentColor = GlanciColors.onSurface,
                    selectedYearContainerColor = GlanciColors.primary,
                    disabledSelectedYearContainerColor = GlanciColors.onSurface,
                    dayContentColor = GlanciColors.onSurface,
                    disabledDayContentColor = GlanciColors.onSurface,
                    selectedDayContentColor = GlanciColors.onPrimary,
                    disabledSelectedDayContentColor = GlanciColors.onSurface,
                    selectedDayContainerColor = GlanciColors.primary,
                    disabledSelectedDayContainerColor = GlanciColors.onSurface,
                    todayContentColor = GlanciColors.primary,
                    todayDateBorderColor = GlanciColors.primary,
                    dayInSelectionRangeContentColor = GlanciColors.onSurface,
                    dayInSelectionRangeContainerColor = GlanciColors.onSurface,
                    dividerColor = GlanciColors.outline,
                    dateTextFieldColors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = GlanciColors.onSurface,
                        unfocusedIndicatorColor = GlanciColors.outline,
                        focusedLabelColor = GlanciColors.onSurface,
                        unfocusedLabelColor = GlanciColors.onSurface,
                        disabledLabelColor = GlanciColors.onSurface
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