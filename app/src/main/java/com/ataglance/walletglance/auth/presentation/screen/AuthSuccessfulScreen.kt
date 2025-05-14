package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.auth.presentation.model.AuthResultSuccessScreenState
import com.ataglance.walletglance.auth.domain.model.AuthResultSuccessScreenType
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.errorHandling.presentation.components.screenContainers.ResultSuccessScreenContainer

@Composable
fun AuthSuccessfulScreen(
    screenType: AuthResultSuccessScreenState,
    onContinueButtonClick: () -> Unit
) {
    ResultSuccessScreenContainer(
        message = stringResource(screenType.getScreenTitleRes()),
        buttonText = stringResource(screenType.getPrimaryButtonTextRes()),
        onContinueButtonClick = onContinueButtonClick
    )
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun AuthSuccessfulScreenPreview() {
    PreviewWithMainScaffoldContainer(appTheme = AppTheme.LightDefault) {
        AuthSuccessfulScreen(
            screenType = AuthResultSuccessScreenState(
                type = AuthResultSuccessScreenType.EmailVerification,
                isAppSetUp = true
            ),
            onContinueButtonClick = {}
        )
    }
}