package com.ataglance.walletglance.core.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.container.LargePrimaryIconWithMessage
import com.ataglance.walletglance.core.presentation.component.screenContainers.PreviewContainer
import com.ataglance.walletglance.core.presentation.component.screenContainers.ScreenContainer
import com.ataglance.walletglance.core.presentation.navigation.SetBackHandler

@Composable
fun UpdateRequestScreen() {
    SetBackHandler()

    ScreenContainer {
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