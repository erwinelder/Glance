package com.ataglance.walletglance.core.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.container.LargePrimaryIconWithMessage
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainer
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler

@Composable
fun UpdateRequestScreen(
    screenPadding: PaddingValues = PaddingValues()
) {
    SetBackHandler()

    ScreenContainer(
        screenPadding = screenPadding
    ) {
        LargePrimaryIconWithMessage(
            title = stringResource(R.string.update_required),
            message = stringResource(R.string.app_update_required_message),
            iconRes = R.drawable.error_large_icon,
            iconDescription = "error icon"
        )
    }
}



@Preview(device = PIXEL_7_PRO)
@Composable
fun UpdateRequestScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewContainer(appTheme = appTheme) {
        UpdateRequestScreen()
    }
}