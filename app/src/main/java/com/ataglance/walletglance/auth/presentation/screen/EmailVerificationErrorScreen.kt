package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.errorHandling.presentation.components.screenContainers.ResultMessageScreenContainer

@Composable
fun EmailVerificationErrorScreen(
    onContinueButtonClick: () -> Unit
) {
    ResultMessageScreenContainer(
        message = stringResource(R.string.email_verification_error),
        iconRes = R.drawable.error_icon,
        iconDescription = "Error",
        iconBackgroundGradient = GlanceTheme.errorGradient,
        buttonText = stringResource(R.string.sign_in),
        onContinueButtonClick = onContinueButtonClick
    )
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EmailVerificationErrorScreenPreview() {
    PreviewWithMainScaffoldContainer(appTheme = AppTheme.LightDefault) {
        EmailVerificationErrorScreen(
            onContinueButtonClick = {}
        )
    }
}