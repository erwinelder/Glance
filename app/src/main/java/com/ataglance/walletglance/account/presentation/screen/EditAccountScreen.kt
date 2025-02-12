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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.domain.utils.getAccountColorsWithNames
import com.ataglance.walletglance.account.mapper.toEditAccountUiState
import com.ataglance.walletglance.account.presentation.model.EditAccountUiState
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.components.buttons.ColorButton
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SecondaryButton
import com.ataglance.walletglance.core.presentation.components.fields.FieldLabel
import com.ataglance.walletglance.core.presentation.components.fields.TextFieldWithLabel
import com.ataglance.walletglance.core.presentation.components.pickers.ColorPicker
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceScreenContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.components.switches.SwitchWithLabel
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun EditAccountScreen(
    scaffoldPadding: PaddingValues,
    editAccountUiState: EditAccountUiState,
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
    var showColorPicker by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        GlassSurfaceScreenContainer(
            topPadding = scaffoldPadding.calculateTopPadding(),
            fillGlassSurface = false,
            topButton = if (allowDeleting) {
                {
                    SecondaryButton(text = stringResource(R.string.delete)) {
                        onDeleteButton(editAccountUiState.id)
                    }
                }
            } else null,
            glassSurfaceContent = {
                GlassSurfaceContent(
                    uiState = editAccountUiState,
                    onColorButtonClick = { showColorPicker = true },
                    onNameChange = onNameChange,
                    onNavigateToEditAccountCurrencyScreen = onNavigateToEditAccountCurrencyScreen,
                    onBalanceChange = onBalanceChange,
                    onHideChange = onHideChange,
                    onHideBalanceChange = onHideBalanceChange,
                    onWithoutBalanceChange = onWithoutBalanceChange
                )
            },
            primaryBottomButton = {
                PrimaryButton(
                    text = stringResource(R.string.save),
                    enabled = allowSaving,
                    onClick = onSaveButton
                )
            }
        )
        ColorPicker(
            visible = showColorPicker,
            colorList = getAccountColorsWithNames(CurrAppTheme),
            onColorClick = onColorChange,
            onPickerClose = {
                showColorPicker = false
            }
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    uiState: EditAccountUiState,
    onColorButtonClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onNavigateToEditAccountCurrencyScreen: () -> Unit,
    onBalanceChange: (String) -> Unit,
    onHideChange: (Boolean) -> Unit,
    onHideBalanceChange: (Boolean) -> Unit,
    onWithoutBalanceChange: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    val verticalGap = dimensionResource(R.dimen.field_gap)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 12.dp, vertical = 24.dp)
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
                color = GlanceColors.onSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .bounceClickEffect(onClick = onNavigateToCurrencyPickerWindow)
                    .clip(RoundedCornerShape(15.dp))
                    .background(GlanceColors.surface)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
            Icon(
                painter = painterResource(R.drawable.short_arrow_right_icon),
                contentDescription = "Right arrow icon",
                tint = GlanceColors.onSurface,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EditAccountScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    account: Account = Account(
        id = 1,
        orderNum = 1,
        name = "Main USD",
        currency = "USD",
        balance = 112.13,
        color = AccountColors.Default,
        isActive = false
    ),
    allowDeleting: Boolean = false,
) {
    val editAccountUiState = account.toEditAccountUiState()

    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        EditAccountScreen(
            scaffoldPadding = scaffoldPadding,
            editAccountUiState = editAccountUiState,
            allowDeleting = allowDeleting,
            allowSaving = editAccountUiState.allowSaving(),
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
