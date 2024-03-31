package com.ataglance.walletglance.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.model.AccountColorName
import com.ataglance.walletglance.model.AccountColors
import com.ataglance.walletglance.model.AccountController
import com.ataglance.walletglance.model.EditAccountUiState
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.SecondaryButton
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface
import com.ataglance.walletglance.ui.theme.uielements.fields.CustomTextFieldWithLabel
import com.ataglance.walletglance.ui.theme.uielements.fields.FieldLabel
import com.ataglance.walletglance.ui.theme.uielements.switches.SwitchBlock

@Composable
fun EditAccountScreen(
    scaffoldPadding: PaddingValues,
    uiState: EditAccountUiState,
    appTheme: AppTheme?,
    showDeleteAccountButton: Boolean,
    onColorChange: (AccountColorName) -> Unit,
    onNameChange: (String) -> Unit,
    onNavigateToCurrencyPickerWindow: () -> Unit,
    onBalanceChange: (String) -> Unit,
    onHideChange: (Boolean) -> Unit,
    onHideBalanceChange: (Boolean) -> Unit,
    onWithoutBalanceChange: (Boolean) -> Unit,
    onSaveButton: () -> Unit,
    onDeleteButton: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val showColorPicker = remember { mutableStateOf(false) }

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
                        ColorButton(uiState.color, appTheme) {
                            showColorPicker.value = true
                        }
                        CustomTextFieldWithLabel(
                            text = uiState.name,
                            onValueChange = onNameChange,
                            labelText = stringResource(R.string.name)
                        )
                        CurrencyField(uiState.currency, onNavigateToCurrencyPickerWindow)
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
            AccountColorPicker(
                visible = showColorPicker.value,
                appTheme = appTheme,
                onColorChange = {
                    onColorChange(it)
                },
                onPickerClose = {
                    showColorPicker.value = false
                }
            )
        }

    }
}

@Composable
private fun ColorButton(
    colorName: String,
    appTheme: AppTheme?,
    onClick: () -> Unit
) {
    val color by animateColorAsState(
        targetValue = AccountController().getAccountAndOnAccountColor(
            colorName, appTheme
        ).first.lighter,
        label = "account color picker button color"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FieldLabel(text = stringResource(R.string.color))
        Spacer(
            modifier = Modifier
                .bounceClickEffect(.97f, onClick = onClick)
                .clip(RoundedCornerShape(15.dp))
                .background(color)
                .size(70.dp, 42.dp)
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AccountColorPicker(
    visible: Boolean,
    appTheme: AppTheme,
    onColorChange: (AccountColorName) -> Unit,
    onPickerClose: () -> Unit
) {
    val accountColors = listOf(
        AccountColors.Default(appTheme),
        AccountColors.Pink(appTheme),
        AccountColors.Blue(appTheme),
        AccountColors.Camel(appTheme),
        AccountColors.Red(appTheme),
        AccountColors.Green(appTheme)
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(300))
    ) {
        Box(
            modifier = Modifier
                .clickable { onPickerClose() }
                .fillMaxSize()
                .background(Color.Black.copy(.2f))
        )
    }
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            initialScale = .5f
        ) + slideInHorizontally { (it * 1.25).toInt() },
        exit = scaleOut(
            targetScale = .5f
        ) + slideOutHorizontally { (it * 1.25).toInt() },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(dimensionResource(R.dimen.record_corner_size)))
                .background(GlanceTheme.background)
                .fillMaxWidth(.8f)
                .padding(12.dp)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                accountColors.forEach { color ->
                    Spacer(
                        modifier = Modifier
                            .bounceClickEffect(.97f) {
                                onColorChange(color.name)
                                onPickerClose()
                            }
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.field_corners)))
                            .fillMaxWidth(.22f)
                            .aspectRatio(1.4f, false)
                            .background(color.color.lighter)
                    )
                }
            }
        }
    }
}
