package com.ataglance.walletglance.transfer.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.presentation.component.AccountPicker
import com.ataglance.walletglance.account.presentation.component.SmallAccountComponent
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.AppUiState
import com.ataglance.walletglance.core.domain.app.DrawableResByTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.keyboardManagement.KeyboardTypingAnimatedVisibilityContainer
import com.ataglance.walletglance.core.presentation.component.container.keyboardManagement.KeyboardTypingAnimatedVisibilitySpacer
import com.ataglance.walletglance.core.presentation.component.divider.SmallDivider
import com.ataglance.walletglance.core.presentation.component.field.DateField
import com.ataglance.walletglance.core.presentation.component.field.FieldWithLabelWrapper
import com.ataglance.walletglance.core.presentation.component.field.SmallTextFieldWithLabel
import com.ataglance.walletglance.core.presentation.component.picker.CustomDatePicker
import com.ataglance.walletglance.core.presentation.component.picker.CustomTimePicker
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTopBackNavButton
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.transaction.presentation.component.TransactionCreationBottomButtonsBlock
import com.ataglance.walletglance.transfer.presentation.model.TransferDraft
import com.ataglance.walletglance.transfer.presentation.model.TransferDraftItem
import com.ataglance.walletglance.transfer.presentation.viewmodel.TransferCreationViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TransferCreationScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    backStack: NavBackStackEntry,
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appUiState: AppUiState
) {
    val transferId = backStack.toRoute<MainScreens.TransferCreation>().transferId

    val viewModel = koinViewModel<TransferCreationViewModel> {
        parametersOf(
            transferId,
            appUiState.accountsAndActiveOne.activeAccount?.id
        )
    }

    val transferDraft by viewModel.transferDraft.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    TransferCreationScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        transferDraft = transferDraft,
        accountList = appUiState.accountsAndActiveOne.accounts,
        onSelectNewDate = viewModel::selectNewDate,
        onSelectNewTime = viewModel::selectNewTime,
        onSelectAnotherAccount = viewModel::selectAnotherAccount,
        onSelectAccount = viewModel::selectAccount,
        onRateChange = viewModel::changeRate,
        onAmountChange = viewModel::changeAmount,
        onSaveButton = {
            coroutineScope.launch {
                viewModel.saveTransfer()
                navViewModel.popBackStackToHomeScreen(navController)
            }
        },
        onRepeatButton = {
            coroutineScope.launch {
                viewModel.repeatTransfer()
                navViewModel.popBackStackToHomeScreen(navController)
            }
        },
        onDeleteButton = {
            coroutineScope.launch {
                viewModel.deleteTransfer()
                navViewModel.popBackStackToHomeScreen(navController)
            }
        }
    )
}

@Composable
fun TransferCreationScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    transferDraft: TransferDraft,
    accountList: List<Account>,
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
    val backNavButtonImageRes = DrawableResByTheme(
        lightDefault = R.drawable.make_transfer_light_default,
        darkDefault = R.drawable.make_transfer_dark_default
    ).get(CurrAppTheme)

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showSenderAccountPicker by remember { mutableStateOf(false) }
    var showReceiverAccountPicker by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenContainerWithTopBackNavButton(
            screenPadding = screenPadding,
            backNavButtonText = stringResource(R.string.make_transfer),
            backNavButtonImageRes = backNavButtonImageRes,
            onBackNavButtonClick = onNavigateBack
        ) { keyboardInFocus ->

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                GlassSurface(
                    modifier = Modifier.weight(1f, fill = false),
                    filledWidths = FilledWidthByScreenType(compact = .86f)
                ) {
                    GlassSurfaceContent(
                        transferDraft = transferDraft,
                        onDateFieldClick = { showDatePicker = true },
                        onAccountFieldClick = { isSender ->
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
                }
            }

            KeyboardTypingAnimatedVisibilitySpacer(isVisible = !keyboardInFocus, height = 24.dp)

            KeyboardTypingAnimatedVisibilityContainer(isVisible = !keyboardInFocus) {
                TransactionCreationBottomButtonsBlock(
                    showOnlySaveButton = transferDraft.isNew,
                    singlePrimaryButtonStringRes = R.string.make_transfer,
                    onSaveButton = onSaveButton,
                    onRepeatButton = onRepeatButton,
                    onDeleteButton = onDeleteButton,
                    savingAndRepeatingAreAllowed = transferDraft.savingIsAllowed
                )
            }

        }
        CustomDatePicker(
            openDialog = showDatePicker,
            initialTimestamp = transferDraft.dateTimeState.timestamp,
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
        Spacer(modifier = Modifier.height(4.dp))
        DateField(
            formattedDate = transferDraft.dateTimeState.dateFormatted,
            onClick = onDateFieldClick
        )
        FieldWithLabelWrapper(labelText = stringResource(R.string.from_account)) {
            AnimatedContent(
                targetState = transferDraft.sender.account
            ) { targetAccount ->
                SmallAccountComponent(account = targetAccount) {
                    onAccountFieldClick(true)
                }
            }
        }
        FieldWithLabelWrapper(labelText = stringResource(R.string.to_account)) {
            AnimatedContent(
                targetState = transferDraft.receiver.account
            ) { targetAccount ->
                SmallAccountComponent(account = targetAccount) {
                    onAccountFieldClick(false)
                }
            }
        }
        SmallDivider(Modifier.padding(top = 4.dp))
        SmallTextFieldWithLabel(
            text = transferDraft.sender.rate,
            onValueChange = { onChangeRate(it, true) },
            labelText = stringResource(R.string.rate),
            placeholderText = "1.00",
            fontSize = 18.sp,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
        SmallTextFieldWithLabel(
            text = transferDraft.sender.amount,
            onValueChange = { onChangeAmount(it, true) },
            labelText = stringResource(R.string.start_amount),
            placeholderText = "0.00",
            fontSize = 20.sp,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
        SmallTextFieldWithLabel(
            text = transferDraft.receiver.amount,
            onValueChange = { onChangeAmount(it, false) },
            labelText = stringResource(R.string.final_amount),
            placeholderText = "0.00",
            fontSize = 20.sp,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
        SmallTextFieldWithLabel(
            text = transferDraft.receiver.rate,
            onValueChange = { onChangeRate(it, false) },
            labelText = stringResource(R.string.rate),
            placeholderText = "1.00",
            fontSize = 18.sp,
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = Modifier.height(6.dp))
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
        id = 0,
        sender = TransferDraftItem(
            account = accountList[0],
            amount = "300.0",
        ),
        receiver = TransferDraftItem(
            account = accountList[1],
            amount = "100.0",
        ),
        savingIsAllowed = true
    )
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        TransferCreationScreen(
            onNavigateBack = {},
            transferDraft = transferDraft,
            accountList = accountList,
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
