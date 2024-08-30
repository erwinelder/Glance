package com.ataglance.walletglance.settings.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.settings.domain.ThemeUiState
import com.ataglance.walletglance.settings.presentation.components.ThemePicker

@Composable
fun AppearanceScreen(
    isAppSetUp: Boolean,
    themeUiState: ThemeUiState,
    onContinueSetupButton: () -> Unit,
    onSetUseDeviceTheme: (Boolean) -> Unit,
    onChooseLightTheme: (String) -> Unit,
    onChooseDarkTheme: (String) -> Unit
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
                onClick = onContinueSetupButton,
                text = stringResource(R.string._continue)
            )
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun AppearanceScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    isSetupProgressTopBarVisible: Boolean = false,
    isBottomBarVisible: Boolean = true,
    themeUiState: ThemeUiState = ThemeUiState(
        useDeviceTheme = false,
        chosenLightTheme = AppTheme.LightDefault.name,
        chosenDarkTheme = AppTheme.DarkDefault.name,
        lastChosenTheme = AppTheme.LightDefault.name
    )
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible
    ) {
        AppearanceScreen(
            isAppSetUp = isAppSetUp,
            themeUiState = themeUiState,
            onContinueSetupButton = {},
            onSetUseDeviceTheme = {},
            onChooseLightTheme = {},
            onChooseDarkTheme = {}
        )
    }
}
