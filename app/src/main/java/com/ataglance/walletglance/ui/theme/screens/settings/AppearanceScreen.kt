package com.ataglance.walletglance.ui.theme.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.model.ThemeUiState
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.pickers.ThemePicker

@Composable
fun SetupAppearanceScreen(
    isAppSetUp: Boolean,
    onContinueButton: () -> Unit,
    onSetUseDeviceTheme: (Boolean) -> Unit,
    onChooseLightTheme: (String) -> Unit,
    onChooseDarkTheme: (String) -> Unit,
    themeUiState: ThemeUiState
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = dimensionResource(R.dimen.screen_vertical_padding))
    ) {
        Spacer(modifier = Modifier.weight(1f))
        ThemePicker(
            onChooseLightTheme = onChooseLightTheme,
            onChooseDarkTheme = onChooseDarkTheme,
            onSetUseDeviceTheme = onSetUseDeviceTheme,
            themeUiState = themeUiState
        )
        Spacer(modifier = Modifier.weight(1f))
        if (!isAppSetUp) {
            PrimaryButton(
                onClick = onContinueButton,
                text = stringResource(R.string._continue)
            )
        }
    }
}