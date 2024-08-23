package com.ataglance.walletglance.core.presentation.components.pickers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState
import com.ataglance.walletglance.core.presentation.components.containers.GlanceDateRangeAssetsPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeAssetsPickerContainer(
    scaffoldPadding: PaddingValues,
    dateRangeMenuUiState: DateRangeMenuUiState,
    openCustomDateRangeWindow: Boolean,
    onCloseCustomDateRangeWindow: () -> Unit,
    onDateRangeSelect: (DateRangeEnum) -> Unit,
    onCustomDateRangeSelect: (Long?, Long?) -> Unit,
) {
    var openDateRangePickerDialog by remember { mutableStateOf(false) }
    val dateRangePickerState = rememberDateRangePickerState()

    LaunchedEffect(dateRangeMenuUiState) {
        dateRangePickerState.setSelection(
            dateRangeMenuUiState.startCalendarDateMillis,
            dateRangeMenuUiState.endCalendarDateMillis
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GlanceDateRangeAssetsPicker(
            visible = openCustomDateRangeWindow,
            padding = PaddingValues(
                top = scaffoldPadding.calculateTopPadding() * 2,
                bottom = scaffoldPadding.calculateBottomPadding() * 2,
                end = 16.dp
            ),
            currentDateRangeEnum = dateRangeMenuUiState.dateRangeWithEnum.enum,
            dateRangePickerState = dateRangePickerState,
            onDismissRequest = onCloseCustomDateRangeWindow,
            onDateRangeSelect = { dateRangeEnum ->
                onDateRangeSelect(dateRangeEnum)
                onCloseCustomDateRangeWindow()
            },
            onCustomDateRangeFieldClick = {
                openDateRangePickerDialog = true
            },
            onConfirmButtonClick = {
                onCloseCustomDateRangeWindow()
                onCustomDateRangeSelect(
                    dateRangePickerState.selectedStartDateMillis,
                    dateRangePickerState.selectedEndDateMillis
                )
            }
        )
        GlanceDateRangePicker(
            visible = openDateRangePickerDialog,
            dateRangePickerState = dateRangePickerState,
            onCloseDialog = {
                openDateRangePickerDialog = false
            }
        )
    }
}