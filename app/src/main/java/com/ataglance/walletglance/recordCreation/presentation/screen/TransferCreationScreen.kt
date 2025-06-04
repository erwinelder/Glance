package com.ataglance.walletglance.recordCreation.presentation.screen

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.presentation.component.AccountPicker
import com.ataglance.walletglance.account.presentation.component.SmallAccountComponent
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.BackButton
import com.ataglance.walletglance.core.presentation.component.divider.SmallDivider
import com.ataglance.walletglance.core.presentation.component.field.DateField
import com.ataglance.walletglance.core.presentation.component.field.FieldWithLabel
import com.ataglance.walletglance.core.presentation.component.field.TextFieldComponent
import com.ataglance.walletglance.core.presentation.component.picker.CustomDatePicker
import com.ataglance.walletglance.core.presentation.component.picker.CustomTimePicker
import com.ataglance.walletglance.core.presentation.component.screenContainer.GlassSurfaceScreenContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.recordCreation.presentation.component.RecordCreationBottomButtonsBlock
import com.ataglance.walletglance.recordCreation.presentation.model.transfer.TransferDraft
import com.ataglance.walletglance.recordCreation.presentation.model.transfer.TransferDraftUnits

@Composable
fun TransferCreationScreen(
    transferDraft: TransferDraft,
    accountList: List<Account>,
    onNavigateBack: () -> Unit,
    onSelectNewDate: (Long) -> Unit,
    onSelectNewTime: (Int, Int) -> Unit,
    onSelectAnotherAccount: (Boolean) -> Unit,
    onSelectAccount: (Account, Boolean) -> Unit,
    onRateChange: (String, Boolean) -> Unit,
    onAmountChange: (String, Boolean) -> Unit,
    onSaveButton: () -> Unit,
    onRepeatButton: () -> Unit,
    onDeleteButton: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showSenderAccountPicker by remember { mutableStateOf(false) }
    var showReceiverAccountPicker by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        GlassSurfaceScreenContainer(
            fillGlassSurface = false,
            topButton = {
                BackButton(onNavigateBack)
            },
            glassSurfaceContent = {
                GlassSurfaceContent(
                    transferDraft = transferDraft,
                    onDateFieldClick = { showDatePicker = true },
                    onAccountFieldClick = { isSender: Boolean ->
                        if (accountList.size == 2) {
                            onSelectAnotherAccount(isSender)
                        } else {
                            if (isSender) showSenderAccountPicker = true
                            else showReceiverAccountPicker = true
                        }
                    },
                    onChangeRate = onRateChange,
                    onChangeAmount = onAmountChange
                )
            },
            primaryBottomButton = {
                RecordCreationBottomButtonsBlock(
                    showOnlySaveButton = transferDraft.isNew,
                    singlePrimaryButtonStringRes = R.string.make_transfer,
                    onSaveButton = onSaveButton,
                    onRepeatButton = onRepeatButton,
                    onDeleteButton = onDeleteButton,
                    savingAndRepeatingAreAllowed = transferDraft.savingIsAllowed
                )
            }
        )
        CustomDatePicker(
            openDialog = showDatePicker,
            initialTimeInMillis = transferDraft.dateTimeState.getTimeInMillis(),
            onOpenDateDialogChange = {
                showDatePicker = it
            },
            onConfirmButton = { timeInMillis ->
                onSelectNewDate(timeInMillis)
                showDatePicker = false
                showTimePicker = true
            }
        )
        CustomTimePicker(
            openDialog = showTimePicker,
            initialHourAndMinute = transferDraft.dateTimeState.getHourAndMinute(),
            onOpenDialogChange = {
                showTimePicker = it
            },
            onConfirmButton = { hour, minute ->
                onSelectNewTime(hour, minute)
                showTimePicker = false
            }
        )
        AccountPicker(
            visible = showSenderAccountPicker || showReceiverAccountPicker,
            accountList = accountList,
            onDismissRequest = {
                if (showSenderAccountPicker) {
                    showSenderAccountPicker = false
                }
                if (showReceiverAccountPicker) {
                    showReceiverAccountPicker = false
                }
            },
            onAccountChoose = { account ->
                onSelectAccount(account, showSenderAccountPicker)
            }
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    transferDraft: TransferDraft,
    onDateFieldClick: () -> Unit,
    onAccountFieldClick: (Boolean) -> Unit,
    onChangeRate: (String, Boolean) -> Unit,
    onChangeAmount: (String, Boolean) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        DateField(
            formattedDate = transferDraft.dateTimeState.dateFormatted,
            onClick = onDateFieldClick
        )
        FieldWithLabel(labelText = stringResource(R.string.from_account)) {
            AnimatedContent(
                targetState = transferDraft.sender.account,
                label = "sender account field at the transfer creation screen"
            ) { targetAccount ->
                SmallAccountComponent(account = targetAccount) {
                    onAccountFieldClick(true)
                }
            }
        }
        FieldWithLabel(labelText = stringResource(R.string.to_account)) {
            AnimatedContent(
                targetState = transferDraft.receiver.account,
                label = "receiver account field at the transfer creation screen"
            ) { targetAccount ->
                SmallAccountComponent(account = targetAccount) {
                    onAccountFieldClick(false)
                }
            }
        }
        SmallDivider(Modifier.padding(top = 4.dp))
        FieldWithLabel(labelText = stringResource(R.string.rate)) {
            TextFieldComponent(
                text = transferDraft.sender.rate,
                placeholderText = "1.00",
                fontSize = 18.sp,
                onValueChange = { onChangeRate(it, true) },
                keyboardType = KeyboardType.Number
            )
        }
        FieldWithLabel(labelText = stringResource(R.string.start_amount)) {
            TextFieldComponent(
                text = transferDraft.sender.amount,
                placeholderText = "0.00",
                fontSize = 22.sp,
                onValueChange = { onChangeAmount(it, true) },
                keyboardType = KeyboardType.Number
            )
        }
        FieldWithLabel(labelText = stringResource(R.string.final_amount)) {
            TextFieldComponent(
                text = transferDraft.receiver.amount,
                placeholderText = "0.00",
                fontSize = 22.sp,
                onValueChange = { onChangeAmount(it, false) },
                keyboardType = KeyboardType.Number
            )
        }
        FieldWithLabel(labelText = stringResource(R.string.rate)) {
            TextFieldComponent(
                text = transferDraft.receiver.rate,
                placeholderText = "1.00",
                fontSize = 18.sp,
                onValueChange = { onChangeRate(it, false) },
                keyboardType = KeyboardType.Number
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun TransferCreationScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    accountList: List<Account> = listOf(
        Account(
            id = 1, color = AccountColors.Pink,
            name = "Main account"
        ),
        Account(
            id = 2, color = AccountColors.Blue,
            name = "Czech Local Card", currency = "CZK"
        )
    ),
    transferDraft: TransferDraft = TransferDraft(
        isNew = true,
        sender = TransferDraftUnits(
            account = accountList[0],
            recordNum = 0,
            amount = "300.0",
        ),
        receiver = TransferDraftUnits(
            account = accountList[1],
            recordNum = 0,
            amount = "100.0",
        ),
        savingIsAllowed = true
    )
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        TransferCreationScreen(
            transferDraft = transferDraft,
            accountList = accountList,
            onNavigateBack = {},
            onSelectNewDate = {},
            onSelectNewTime = { _, _ -> },
            onSelectAnotherAccount = {},
            onSelectAccount = { _, _ -> },
            onRateChange = { _, _ -> },
            onAmountChange = { _, _ -> },
            onSaveButton = {},
            onRepeatButton = {},
            onDeleteButton = {}
        )
    }
}
