package com.ataglance.walletglance.errorHandling.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.auth.domain.model.AuthResultSuccessScreenType
import com.ataglance.walletglance.auth.presentation.model.AuthResultSuccessScreenState
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.errorHandling.presentation.components.screenContainers.ResultSuccessScreenContainer

@Composable
fun AuthResultSuccessScreen(
    screenState: AuthResultSuccessScreenState,
    onContinueButtonClick: () -> Unit
) {
    ResultSuccessScreenContainer(
        message = stringResource(screenState.getScreenTitleRes()),
        buttonText = stringResource(screenState.getPrimaryButtonTextRes()),
        onContinueButtonClick = onContinueButtonClick
    )
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun AuthResultSuccessScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    screenType: AuthResultSuccessScreenType = AuthResultSuccessScreenType.PasswordUpdate
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        AuthResultSuccessScreen(
            screenState = AuthResultSuccessScreenState.fromString(
                type = screenType.name,
                isAppSetUp = true
            ),
            onContinueButtonClick = {}
        )
    }
}