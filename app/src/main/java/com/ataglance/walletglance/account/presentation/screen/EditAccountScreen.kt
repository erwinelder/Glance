package com.ataglance.walletglance.account.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.domain.navigation.AccountsSettingsScreens
import com.ataglance.walletglance.account.domain.utils.getAccountColorsWithNames
import com.ataglance.walletglance.account.mapper.toDraft
import com.ataglance.walletglance.account.presentation.model.AccountDraft
import com.ataglance.walletglance.account.presentation.viewmodel.EditAccountViewModel
import com.ataglance.walletglance.account.presentation.viewmodel.EditAccountsViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.ColorButton
import com.ataglance.walletglance.core.presentation.component.button.TertiaryButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurface
import com.ataglance.walletglance.core.presentation.component.field.FieldLabel
import com.ataglance.walletglance.core.presentation.component.field.TextFieldWithLabel
import com.ataglance.walletglance.core.presentation.component.picker.ColorPicker
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTopBackNavButtonAndPrimaryButton
import com.ataglance.walletglance.core.presentation.component.switchButton.SwitchWithLabel
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.core.utils.takeRowComposableIf
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory

@Composable
fun EditAccountScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    backStack: NavBackStackEntry,
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    val accountsViewModel = backStack.sharedKoinNavViewModel<EditAccountsViewModel>(navController)
    val accountViewModel = backStack.sharedViewModel<EditAccountViewModel>(navController)

    val accountDraft by accountViewModel.accountDraft.collectAsStateWithLifecycle()
    val allowDeleting by accountsViewModel.allowDeleting.collectAsStateWithLifecycle()
    val allowSaving by accountViewModel.allowSaving.collectAsStateWithLifecycle()

    EditAccountScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        accountDraft = accountDraft,
        allowDeleting = allowDeleting && !accountDraft.isNew(),
        allowSaving = allowSaving,
        onColorChange = accountViewModel::changeColor,
        onNameChange = accountViewModel::changeName,
        onNavigateToEditAccountCurrencyScreen = {
            navViewModel.navigateToScreen(
                navController = navController,
                screen = AccountsSettingsScreens.EditAccountCurrency(
                    currency = accountDraft.currency
                )
            )
        },
        onBalanceChange = accountViewModel::changeBalance,
        onHideChange = accountViewModel::changeHideStatus,
        onHideBalanceChange = accountViewModel::changeHideBalanceStatus,
        onWithoutBalanceChange = accountViewModel::changeWithoutBalanceStatus,
        onDeleteButton = { accountId ->
            navController.popBackStack()
            accountsViewModel.deleteAccount(id = accountId)
        },
        onSaveButton = {
            accountViewModel.getAccount()?.let(accountsViewModel::applyAccount)
            navController.popBackStack()
        }
    )
}

@Composable
fun EditAccountScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    accountDraft: AccountDraft,
    allowDeleting: Boolean,
    allowSaving: Boolean,
    onColorChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onNavigateToEditAccountCurrencyScreen: () -> Unit,
    onBalanceChange: (String) -> Unit,
    onHideChange: (Boolean) -> Unit,
    onHideBalanceChange: (Boolean) -> Unit,
    onWithoutBalanceChange: (Boolean) -> Unit,
    onSaveButton: () -> Unit,
    onDeleteButton: (Int) -> Unit
) {
    val screenIcon = SettingsCategory.Accounts(appTheme = CurrAppTheme)
    val backNavButtonText = accountDraft.name.takeIf { it.isNotBlank() }
        ?: stringResource(R.string.account)

    var showColorPicker by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenContainerWithTopBackNavButtonAndPrimaryButton(
            screenPadding = screenPadding,
            backNavButtonText = backNavButtonText,
            backNavButtonImageRes = screenIcon.iconRes,
            onBackNavButtonClick = onNavigateBack,
            backNavButtonCompanionComponent = takeRowComposableIf(allowDeleting) {
                TertiaryButton(
                    text = stringResource(R.string.delete),
                    iconRes = R.drawable.trash_icon
                ) {
                    onDeleteButton(accountDraft.id)
                }
            },
            primaryButtonText = stringResource(R.string.save),
            primaryButtonEnabled = allowSaving,
            onPrimaryButtonClick = onSaveButton
        ) {
            GlassSurface {
                GlassSurfaceContent(
                    uiState = accountDraft,
                    onColorButtonClick = { showColorPicker = true },
                    onNameChange = onNameChange,
                    onNavigateToEditAccountCurrencyScreen = onNavigateToEditAccountCurrencyScreen,
                    onBalanceChange = onBalanceChange,
                    onHideChange = onHideChange,
                    onHideBalanceChange = onHideBalanceChange,
                    onWithoutBalanceChange = onWithoutBalanceChange
                )
            }
        }
        ColorPicker(
            visible = showColorPicker,
            colorList = getAccountColorsWithNames(CurrAppTheme),
            onColorClick = onColorChange,
            onPickerClose = { showColorPicker = false }
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    uiState: AccountDraft,
    onColorButtonClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onNavigateToEditAccountCurrencyScreen: () -> Unit,
    onBalanceChange: (String) -> Unit,
    onHideChange: (Boolean) -> Unit,
    onHideBalanceChange: (Boolean) -> Unit,
    onWithoutBalanceChange: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    val verticalGap = 16.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        ColorButton(
            color = uiState.color.getColorAndColorOnByTheme(CurrAppTheme).first.lighter,
            onClick = onColorButtonClick
        )
        Spacer(modifier = Modifier.height(verticalGap))
        TextFieldWithLabel(
            text = uiState.name,
            placeholderText = stringResource(R.string.account_name),
            onValueChange = onNameChange,
            labelText = stringResource(R.string.name)
        )
        Spacer(modifier = Modifier.height(verticalGap))
        CurrencyField(uiState.currency, onNavigateToEditAccountCurrencyScreen)
        AnimatedVisibility(visible = !uiState.withoutBalance) {
            Column {
                Spacer(modifier = Modifier.height(verticalGap))
                TextFieldWithLabel(
                    text = uiState.balance,
                    onValueChange = onBalanceChange,
                    keyboardType = KeyboardType.Number,
                    labelText = stringResource(R.string.balance)
                )
            }
        }
        Spacer(modifier = Modifier.height(verticalGap))
        SwitchWithLabel(
            checked = uiState.hide,
            onCheckedChange = onHideChange,
            labelText = stringResource(R.string.hide_from_top_bar)
        )
        Spacer(modifier = Modifier.height(verticalGap))
        SwitchWithLabel(
            checked = uiState.hideBalance,
            onCheckedChange = onHideBalanceChange,
            labelText = stringResource(R.string.hide_balance)
        )
        Spacer(modifier = Modifier.height(verticalGap))
        SwitchWithLabel(
            checked = uiState.withoutBalance,
            onCheckedChange = onWithoutBalanceChange,
            labelText = stringResource(R.string.without_balance)
        )
    }
}

@Composable
private fun CurrencyField(currency: String, onNavigateToCurrencyPickerWindow: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FieldLabel(text = stringResource(R.string.currency))
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currency,
                color = GlanciColors.onSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .bounceClickEffect(onClick = onNavigateToCurrencyPickerWindow)
                    .clip(RoundedCornerShape(15.dp))
                    .background(GlanciColors.surface)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
            Icon(
                painter = painterResource(R.drawable.short_arrow_right_icon),
                contentDescription = "Right arrow icon",
                tint = GlanciColors.onSurface,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EditAccountScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    account: Account = Account(
        id = 1,
        orderNum = 1,
        name = "Main USD",
        currency = "USD",
        balance = 112.13,
        color = AccountColors.Default,
        isActive = false
    ),
    allowDeleting: Boolean = true
) {
    val accountDraft = account.toDraft()

    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        EditAccountScreen(
            screenPadding = scaffoldPadding,
            onNavigateBack = {},
            accountDraft = accountDraft,
            allowDeleting = allowDeleting && !accountDraft.isNew(),
            allowSaving = accountDraft.allowSaving(),
            onColorChange = {},
            onNameChange = {},
            onNavigateToEditAccountCurrencyScreen = {},
            onBalanceChange = {},
            onHideChange = {},
            onHideBalanceChange = {},
            onWithoutBalanceChange = {},
            onSaveButton = {},
            onDeleteButton = {}
        )
    }
}
