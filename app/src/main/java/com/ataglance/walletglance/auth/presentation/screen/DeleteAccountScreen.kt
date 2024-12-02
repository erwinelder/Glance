package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.containers.DangerousActionBlock
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer

@Composable
fun DeleteAccountScreen(onDeleteAccount: () -> Unit) {
    DangerousActionBlock(
        actionText = stringResource(R.string.delete_account_action),
        actionConfirmationText = stringResource(R.string.delete_account_confirmation),
        actionButtonText = stringResource(R.string.delete_account),
        onAction = onDeleteAccount
    )
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun DeleteAccountScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isBottomBarVisible: Boolean = true,
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isBottomBarVisible = isBottomBarVisible
    ) {
        DeleteAccountScreen(
            onDeleteAccount = {}
        )
    }
}