package com.ataglance.walletglance.core.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.request.presentation.component.screenContainer.ResultScreenContainer

@Composable
fun SetupFinishScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onFinishSetupButton: () -> Unit
) {
    ResultScreenContainer(
        screenPadding = screenPadding,
        iconPathsRes = IconPathsRes.Success,
        title = stringResource(R.string.all_set),
        iconGradientColor = GlanciColors.iconPrimaryGlassGradientPair,
        buttonText = stringResource(R.string.continue_to_app),
        onPrimaryButtonClick = onFinishSetupButton
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
