package com.ataglance.walletglance.core.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.request.presentation.component.screenContainer.ResultSuccessScreenContainer

@Composable
fun SetupFinishScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onFinishSetupButton: () -> Unit
) {
    ResultSuccessScreenContainer(
        screenPadding = screenPadding,
        title = stringResource(R.string.all_set),
        buttonText = stringResource(R.string.continue_to_app),
        onContinueButtonClick = onFinishSetupButton
    )
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun FinishSetupScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        SetupFinishScreen(
            onFinishSetupButton = {}
        )
    }
}
