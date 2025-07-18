package com.ataglance.walletglance.settings.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.container.DangerousActionBlock
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory
import com.ataglance.walletglance.settings.presentation.screenContainer.SettingsCategoryScreenContainer
import com.ataglance.walletglance.settings.presentation.viewmodel.ResetDataViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResetDataScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavController
) {
    val viewModel = koinViewModel<ResetDataViewModel>()

    val coroutineScope = rememberCoroutineScope()

    ResetDataScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        onResetData = {
            coroutineScope.launch {
                viewModel.resetData()
            }
        }
    )
}

@Composable
fun ResetDataScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    onResetData: () -> Unit
) {
    val appTheme = CurrAppTheme
    val thisCategory = remember {
        SettingsCategory.ResetData(appTheme)
    }

    SettingsCategoryScreenContainer(
        screenPadding = screenPadding,
        thisCategory = thisCategory,
        onNavigateBack = onNavigateBack,
        topBottomSpacingProportion = Pair(1f, 1f),
        mainScreenContent = {
            DangerousActionBlock(
                actionText = stringResource(R.string.reset_data_action),
                actionConfirmationText = stringResource(R.string.reset_data_confirmation),
                actionButtonText = stringResource(R.string.reset_data),
                onAction = onResetData
            )
        },
        allowScroll = false
    )
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun ResetDataScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        ResetDataScreen(
            screenPadding = it,
            onNavigateBack = {},
            onResetData = {}
        )
    }
}
