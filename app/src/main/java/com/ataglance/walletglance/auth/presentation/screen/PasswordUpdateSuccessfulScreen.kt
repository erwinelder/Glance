package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.errorHandling.presentation.components.screenContainers.SuccessResultScreenContainer

@Composable
fun PasswordUpdateSuccessfulScreen(
    isAppSetUp: Boolean,
    onContinueButtonClick: () -> Unit
) {
    SuccessResultScreenContainer(
        message = stringResource(R.string.password_update_success),
        buttonText = stringResource(
            if (isAppSetUp) R.string.continue_to_app else R.string.continue_setup
        ),
        onContinueButtonClick = onContinueButtonClick
    )
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun PasswordUpdateSuccessfulScreenPreview() {
    PreviewWithMainScaffoldContainer(appTheme = AppTheme.LightDefault) {
        PasswordUpdateSuccessfulScreen(
            isAppSetUp = true,
            onContinueButtonClick = {}
        )
    }
}