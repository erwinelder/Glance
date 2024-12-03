package com.ataglance.walletglance.errorHandling.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.auth.domain.model.SuccessResultScreenState
import com.ataglance.walletglance.auth.domain.model.SuccessResultScreenType
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.errorHandling.presentation.components.screenContainers.SuccessResultScreenContainer

@Composable
fun SuccessResultScreen(
    screenState: SuccessResultScreenState,
    onContinueButtonClick: () -> Unit
) {
    SuccessResultScreenContainer(
        message = stringResource(screenState.getScreenTitleRes()),
        buttonText = stringResource(screenState.getPrimaryButtonTextRes()),
        onContinueButtonClick = onContinueButtonClick
    )
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun SuccessResultScreenPreview() {
    PreviewWithMainScaffoldContainer(appTheme = AppTheme.LightDefault) {
        SuccessResultScreen(
            screenState = SuccessResultScreenState.fromString(
                type = SuccessResultScreenType.PasswordUpdateSuccess .name,
                isAppSetUp = true
            ),
            onContinueButtonClick = {}
        )
    }
}