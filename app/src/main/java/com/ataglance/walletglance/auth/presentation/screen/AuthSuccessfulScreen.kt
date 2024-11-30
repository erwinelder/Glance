package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.auth.domain.model.AuthSuccessfulScreenType
import com.ataglance.walletglance.auth.domain.model.AuthSuccessfulScreenTypeEnum
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.errorHandling.presentation.components.screenContainers.ResultMessageScreenContainer

@Composable
fun AuthSuccessfulScreen(
    screenType: AuthSuccessfulScreenType,
    onContinueButtonClick: () -> Unit
) {
    ResultMessageScreenContainer(
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
            screenType = AuthSuccessfulScreenType(
                type = AuthSuccessfulScreenTypeEnum.AfterEmailVerification,
                isAppSetUp = true
            ),
            onContinueButtonClick = {}
        )
    }
}