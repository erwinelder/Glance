package com.ataglance.walletglance.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.model.MakeRecordStatus
import com.ataglance.walletglance.model.MakeTransferUiState
import com.ataglance.walletglance.model.MakeTransferViewModel
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.accounts.SmallAccount
import com.ataglance.walletglance.ui.theme.uielements.buttons.BackButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.MakeRecordBottomButtonBlock
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface
import com.ataglance.walletglance.ui.theme.uielements.dividers.SmallDivider
import com.ataglance.walletglance.ui.theme.uielements.fields.CustomTextField
import com.ataglance.walletglance.ui.theme.uielements.fields.DateField
import com.ataglance.walletglance.ui.theme.uielements.fields.MakeRecordFieldContainer
import com.ataglance.walletglance.ui.theme.uielements.pickers.AccountPicker
import com.ataglance.walletglance.ui.theme.uielements.pickers.CustomDatePicker
import com.ataglance.walletglance.ui.theme.uielements.pickers.CustomTimePicker
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeTransferScreen(
    appTheme: AppTheme?,
    viewModel: MakeTransferViewModel,
    makeRecordStatus: String,
    accountList: List<Account>,
    onNavigateBack: () -> Unit,
    onSaveButton: (MakeTransferUiState) -> Unit,
    onRepeatButton: (MakeTransferUiState) -> Unit,
    onDeleteButton: (Int) -> Unit
) {
    val fieldsCornerSize = 15.dp
    val scrollState = rememberScrollState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val savingIsAllowed = uiState.startAmount.isNotBlank() &&
        uiState.startAmount.last() != '.' &&
        uiState.startAmount.toDouble() != 0.0
        uiState.finalAmount.isNotBlank() &&
        uiState.finalAmount.last() != '.' &&
        uiState.finalAmount.toDouble() != 0.0 &&
        uiState.fromAccount != null &&
        uiState.toAccount != null

    val openDateDialog = remember { mutableStateOf(false) }
    val openTimeDialog = remember { mutableStateOf(false) }
    val openDialogFromAccount = remember { mutableStateOf(false) }
    val openDialogToAccount = remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.dateTimeState.calendar.timeInMillis
    )
    val timePickerState = rememberTimePickerState(
        initialHour = uiState.dateTimeState.calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = uiState.dateTimeState.calendar.get(Calendar.MINUTE),
        is24Hour = true
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.buttons_gap)),
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = dimensionResource(R.dimen.screen_vertical_padding))
        ) {

            BackButton(onNavigateBack)

            GlassSurface(modifier = Modifier.weight(1f)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .verticalScroll(scrollState)
                ) {
                    Spacer(modifier = Modifier.height(6.dp))
                    DateField(
                        dateFormatted = uiState.dateTimeState.dateFormatted,
                        cornerSize = fieldsCornerSize,
                        onClick = { openDateDialog.value = true }
                    )
                    MakeRecordFieldContainer(R.string.from_account) {
                        SmallAccount(uiState.fromAccount, appTheme) {
                            openDialogFromAccount.value = true
                        }
                    }
                    MakeRecordFieldContainer(R.string.to_account) {
                        SmallAccount(uiState.toAccount, appTheme) {
                            openDialogToAccount.value = true
                        }
                    }
                    SmallDivider(Modifier.padding(top = 4.dp))
                    MakeRecordFieldContainer(R.string.rate) {
                        CustomTextField(
                            text = uiState.startRate,
                            placeholderText = "1",
                            fontSize = 18.sp,
                            cornerSize = fieldsCornerSize,
                            onValueChange = viewModel::changeStartRate,
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.bounceClickEffect(.97f)
                        )
                    }
                    MakeRecordFieldContainer(R.string.start_amount) {
                        CustomTextField(
                            text = uiState.startAmount,
                            placeholderText = "0.00",
                            fontSize = 22.sp,
                            cornerSize = fieldsCornerSize,
                            onValueChange = viewModel::changeStartAmount,
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.bounceClickEffect(.97f)
                        )
                    }
                    MakeRecordFieldContainer(R.string.final_amount) {
                        CustomTextField(
                            text = uiState.finalAmount,
                            placeholderText = "0.00",
                            fontSize = 22.sp,
                            cornerSize = fieldsCornerSize,
                            onValueChange = viewModel::changeFinalAmount,
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.bounceClickEffect(.97f)
                        )
                    }
                    MakeRecordFieldContainer(R.string.rate) {
                        CustomTextField(
                            text = uiState.finalRate,
                            placeholderText = "1",
                            fontSize = 18.sp,
                            cornerSize = fieldsCornerSize,
                            onValueChange = viewModel::changeFinalRate,
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.bounceClickEffect(.97f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            MakeRecordBottomButtonBlock(
                showSingleButton = makeRecordStatus == MakeRecordStatus.Create.name,
                singlePrimaryButtonStringRes = R.string.make_transfer,
                onSaveButton = { onSaveButton(uiState) },
                onRepeatButton = { onRepeatButton(uiState) },
                onDeleteButton = {
                    uiState.recordNum?.let {
                        onDeleteButton(it)
                    }
                },
                buttonsAreEnabled = savingIsAllowed
            )

        }
        CustomDatePicker(
            openDialog = openDateDialog.value,
            onOpenDateDialogChange = { openDateDialog.value = it },
            onConfirmButton = {
                datePickerState.selectedDateMillis?.let { viewModel.selectNewDate(it) }
                openDateDialog.value = false
                openTimeDialog.value = true
            },
            state = datePickerState
        )
        CustomTimePicker(
            openDialog = openTimeDialog.value,
            onOpenTimeDialogChange = { openTimeDialog.value = it },
            onConfirmButton = {
                viewModel.selectNewTime(timePickerState.hour, timePickerState.minute)
                openTimeDialog.value = false
            },
            state = timePickerState
        )
        AccountPicker(
            visible = openDialogFromAccount.value || openDialogToAccount.value,
            accountList = accountList,
            appTheme = appTheme,
            onDismissRequest = {
                if (openDialogFromAccount.value) {
                    openDialogFromAccount.value = false
                }
                if (openDialogToAccount.value) {
                    openDialogToAccount.value = false
                }
            },
            onAccountChoose = { account ->
                if (openDialogFromAccount.value) {
                    viewModel.chooseFromAccount(account)
                } else if (openDialogToAccount.value) {
                    viewModel.chooseToAccount(account)
                }
            }
        )
    }
}