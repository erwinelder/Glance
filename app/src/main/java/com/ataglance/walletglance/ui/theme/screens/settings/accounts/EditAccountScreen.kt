package com.ataglance.walletglance.ui.theme.screens.settings.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.buttons.ColorButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.SecondaryButton
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface
import com.ataglance.walletglance.ui.theme.uielements.fields.CustomTextFieldWithLabel
import com.ataglance.walletglance.ui.theme.uielements.fields.FieldLabel
import com.ataglance.walletglance.ui.theme.uielements.pickers.ColorPicker
import com.ataglance.walletglance.ui.theme.uielements.switches.SwitchBlock
import com.ataglance.walletglance.ui.utils.getAccountAndOnAccountColor
import com.ataglance.walletglance.data.accounts.AccountColors
import com.ataglance.walletglance.ui.viewmodels.accounts.EditAccountUiState

@Composable
fun EditAccountScreen(
    scaffoldPadding: PaddingValues,
    uiState: EditAccountUiState,
    appTheme: AppTheme?,
    showDeleteAccountButton: Boolean,
    onColorChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onNavigateToEditAccountCurrencyWindow: () -> Unit,
    onBalanceChange: (String) -> Unit,
    onHideChange: (Boolean) -> Unit,
    onHideBalanceChange: (Boolean) -> Unit,
    onWithoutBalanceChange: (Boolean) -> Unit,
    onSaveButton: () -> Unit,
    onDeleteButton: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    var showColorPicker by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = scaffoldPadding.calculateTopPadding() + dimensionResource(R.dimen.screen_vertical_padding),
                    bottom = dimensionResource(R.dimen.screen_vertical_padding)
                )
        ) {
            if (showDeleteAccountButton) {
                SecondaryButton(
                    onClick = { onDeleteButton(uiState.id) },
                    text = stringResource(R.string.delete)
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.buttons_gap)))
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                GlassSurface {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.field_gap)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .height(IntrinsicSize.Min)
                            .verticalScroll(scrollState)
                            .padding(horizontal = 12.dp, vertical = 24.dp)
                    ) {
                        ColorButton(
                            color = getAccountAndOnAccountColor(uiState.colorName, appTheme)
                                .first.lighter
                        ) {
                            showColorPicker = true
                        }
                        CustomTextFieldWithLabel(
                            text = uiState.name,
                            onValueChange = onNameChange,
                            labelText = stringResource(R.string.name)
                        )
                        CurrencyField(uiState.currency, onNavigateToEditAccountCurrencyWindow)
                        CustomTextFieldWithLabel(
                            text = uiState.balance,
                            onValueChange = onBalanceChange,
                            keyboardType = KeyboardType.Number,
                            labelText = stringResource(R.string.balance)
                        )
                        SwitchBlock(
                            checked = uiState.hide,
                            onCheckedChange = onHideChange,
                            labelText = stringResource(R.string.hide_from_top_bar)
                        )
                        SwitchBlock(
                            checked = uiState.hideBalance,
                            onCheckedChange = onHideBalanceChange,
                            labelText = stringResource(R.string.hide_balance)
                        )
                        SwitchBlock(
                            checked = uiState.withoutBalance,
                            onCheckedChange = onWithoutBalanceChange,
                            labelText = stringResource(R.string.without_balance)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.buttons_gap)))
            PrimaryButton(
                onClick = { onSaveButton() },
                text = stringResource(R.string.save),
                enabled = uiState.allowSaving
            )
        }

        if (appTheme != null) {
            ColorPicker(
                visible = showColorPicker,
                colorList = listOf(
                    AccountColors.Default(appTheme).color,
                    AccountColors.Pink(appTheme).color,
                    AccountColors.Blue(appTheme).color,
                    AccountColors.Camel(appTheme).color,
                    AccountColors.Red(appTheme).color,
                    AccountColors.Green(appTheme).color
                ),
                onColorClick = onColorChange,
                onPickerClose = {
                    showColorPicker = false
                }
            )
        }

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
                color = GlanceTheme.onSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .bounceClickEffect(
                        .97f,
                        onClick = onNavigateToCurrencyPickerWindow
                    )
                    .clip(RoundedCornerShape(15.dp))
                    .background(GlanceTheme.surface)
                    .padding(12.dp, 6.dp)
            )
            Icon(
                painter = painterResource(R.drawable.short_arrow_right_icon),
                contentDescription = "Right arrow icon",
                tint = GlanceTheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
