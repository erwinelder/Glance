package com.ataglance.walletglance.settings.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.container.DangerousActionBlock
import com.ataglance.walletglance.core.presentation.component.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.settings.presentation.viewmodel.ResetDataViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResetDataScreenWrapper() {
    val viewModel = koinViewModel<ResetDataViewModel>()

    val coroutineScope = rememberCoroutineScope()

    ResetDataScreen(
        onResetData = {
            coroutineScope.launch {
                viewModel.resetData()
            }
        }
    )
}

@Composable
fun ResetDataScreen(onResetData: () -> Unit) {
    DangerousActionBlock(
        actionText = stringResource(R.string.reset_data_action),
        actionConfirmationText = stringResource(R.string.reset_data_confirmation),
        actionButtonText = stringResource(R.string.reset_data),
        onAction = onResetData
    )
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun ResetDataScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isBottomBarVisible: Boolean = true,
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isBottomBarVisible = isBottomBarVisible
    ) {
        ResetDataScreen(
            onResetData = {}
        )
    }
}
