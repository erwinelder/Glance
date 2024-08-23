package com.ataglance.walletglance.presentation.ui.screens

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.makingRecord.MakeRecordStatus
import com.ataglance.walletglance.presentation.ui.animation.bounceClickEffect
import com.ataglance.walletglance.presentation.ui.uielements.accounts.AccountPicker
import com.ataglance.walletglance.presentation.ui.uielements.accounts.SmallAccount
import com.ataglance.walletglance.presentation.ui.uielements.buttons.BackButton
import com.ataglance.walletglance.presentation.ui.uielements.buttons.MakeRecordBottomButtonBlock
import com.ataglance.walletglance.presentation.ui.uielements.containers.GlassSurface
import com.ataglance.walletglance.presentation.ui.uielements.dividers.SmallDivider
import com.ataglance.walletglance.presentation.ui.uielements.fields.GlanceTextField
import com.ataglance.walletglance.presentation.ui.uielements.fields.DateField
import com.ataglance.walletglance.presentation.ui.uielements.fields.MakeRecordFieldContainer
import com.ataglance.walletglance.presentation.ui.uielements.pickers.CustomDatePicker
import com.ataglance.walletglance.presentation.ui.uielements.pickers.CustomTimePicker
import com.ataglance.walletglance.presentation.viewmodels.records.MakeTransferUiState
import com.ataglance.walletglance.presentation.viewmodels.records.MakeTransferViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeTransferScreen(
    appTheme: AppTheme?,
    viewModel: MakeTransferViewModel,
    makeRecordStatus: MakeRecordStatus,
    accountList: List<Account>,
    onNavigateBack: () -> Unit,
    onSaveButton: (MakeTransferUiState) -> Unit,
    onRepeatButton: (MakeTransferUiState) -> Unit,
    onDeleteButton: (Int) -> Unit
) {
    val fieldsCornerSize = 15.dp
    val scrollState = rememberScrollState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val savingIsAllowed = (uiState.startAmount.isNotBlank() &&
            uiState.finalAmount.isNotBlank() &&
            uiState.startAmount.lastOrNull() != '.' &&
            uiState.startAmount.toDouble() != 0.0 &&
            uiState.finalAmount.lastOrNull() != '.' &&
            uiState.finalAmount.toDouble() != 0.0 &&
            uiState.fromAccount != null &&
            uiState.toAccount != null)

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showFromAccountPicker by remember { mutableStateOf(false) }
    var showToAccountPicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.dateTimeState.calendar.timeInMillis
    )
    val timePickerState = rememberTimePickerState(
        initialHour = uiState.dateTimeState.calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = uiState.dateTimeState.calendar.get(Calendar.MINUTE),
        is24Hour = true
    )

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.buttons_gap)),
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = dimensionResource(R.dimen.screen_vertical_padding))
        ) {

            BackButton(onNavigateBack)

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GlassSurface(modifier = Modifier.weight(1f, fill = false)) {
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
                                onClick = {
                                    showDatePicker = true
                                }
                            )
                            MakeRecordFieldContainer(R.string.from_account) {
                                AnimatedContent(
                                    targetState = uiState.fromAccount,
                                    label = "from account field at the make transfer screen"
                                ) { targetAccount ->
                                    SmallAccount(targetAccount, appTheme) {
                                        if (accountList.size == 2) {
                                            viewModel.changeFromAccount()
                                        } else {
                                            showFromAccountPicker = true
                                        }
                                    }
                                }
                            }
                            MakeRecordFieldContainer(R.string.to_account) {
                                AnimatedContent(
                                    targetState = uiState.toAccount,
                                    label = "to account field at the make transfer screen"
                                ) { targetAccount ->
                                    SmallAccount(targetAccount, appTheme) {
                                        if (accountList.size == 2) {
                                            viewModel.changeToAccount()
                                        } else {
                                            showToAccountPicker = true
                                        }
                                    }
                                }
                            }
                            SmallDivider(Modifier.padding(top = 4.dp))
                            MakeRecordFieldContainer(R.string.rate) {
                                GlanceTextField(
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
                                GlanceTextField(
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
                                GlanceTextField(
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
                                GlanceTextField(
                                    text = uiState.finalRate,
                                    placeholderText = "1",
                                    fontSize = 18.sp,
                                    cornerSize = fieldsCornerSize,
                                    onValueChange = viewModel::changeFinalRate,
                                    keyboardType = KeyboardType.Number,
                                    modifier = Modifier.bounceClickEffect(.97f)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }

            MakeRecordBottomButtonBlock(
                showSingleButton = makeRecordStatus == MakeRecordStatus.Create,
                singlePrimaryButtonStringRes = R.string.make_transfer,
                onSaveButton = { onSaveButton(uiState) },
                onRepeatButton = { onRepeatButton(uiState) },
                onDeleteButton = {
                    uiState.recordNum?.let { onDeleteButton(it) }
                },
                buttonsAreEnabled = savingIsAllowed
            )

        }
        CustomDatePicker(
            openDialog = showDatePicker,
            onOpenDateDialogChange = {
                showDatePicker = it
            },
            onConfirmButton = {
                datePickerState.selectedDateMillis?.let { viewModel.selectNewDate(it) }
                showDatePicker = false
                showTimePicker = true
            },
            state = datePickerState
        )
        CustomTimePicker(
            openDialog = showTimePicker,
            onOpenTimeDialogChange = {
                showTimePicker = it
            },
            onConfirmButton = {
                viewModel.selectNewTime(timePickerState.hour, timePickerState.minute)
                showTimePicker = false
            },
            state = timePickerState
        )
        AccountPicker(
            visible = showFromAccountPicker || showToAccountPicker,
            accountList = accountList,
            appTheme = appTheme,
            onDismissRequest = {
                if (showFromAccountPicker) {
                    showFromAccountPicker = false
                }
                if (showToAccountPicker) {
                    showToAccountPicker = false
                }
            },
            onAccountChoose = { account ->
                if (showFromAccountPicker) {
                    viewModel.chooseFromAccount(account)
                } else if (showToAccountPicker) {
                    viewModel.chooseToAccount(account)
                }
            }
        )
    }
}