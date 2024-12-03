package com.ataglance.walletglance.core.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.errorHandling.presentation.components.screenContainers.SuccessResultScreenContainer

@Composable
fun SetupFinishScreen(
    onFinishSetupButton: () -> Unit
) {
    SuccessResultScreenContainer(
        message = stringResource(R.string.all_set),
        buttonText = stringResource(R.string.continue_to_app),
        onContinueButtonClick = onFinishSetupButton
    )
}



@Preview(
    device = Devices.PIXEL_7_PRO
)
@Composable
fun SetupFinishScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        anyScreenInHierarchyIsScreenProvider = { false },
        currentScreenIsScreenProvider = { false }
    ) {
        SetupFinishScreen(
            onFinishSetupButton = {}
        )
    }
}
