package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.errorHandling.presentation.components.screenContainers.ResultErrorScreenContainer

@Composable
fun EmailVerificationErrorScreen(
    onContinueButtonClick: () -> Unit
) {
    ResultErrorScreenContainer(
        message = stringResource(R.string.email_verification_error),
        buttonText = stringResource(R.string.sign_in),
        onContinueButtonClick = onContinueButtonClick
    )
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EmailVerificationErrorScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        EmailVerificationErrorScreen(
            onContinueButtonClick = {}
        )
    }
}