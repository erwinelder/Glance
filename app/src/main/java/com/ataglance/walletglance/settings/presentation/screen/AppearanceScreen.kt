package com.ataglance.walletglance.settings.presentation.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.CurrAppTheme
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.WindowTypeIsCompact
import com.ataglance.walletglance.core.presentation.components.buttons.GlassSurfaceNavigationButton
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlanceBottomSheet
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.settings.domain.SettingsCategories
import com.ataglance.walletglance.settings.domain.ThemeUiState
import com.ataglance.walletglance.settings.presentation.components.ThemePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceScreen(
    isAppSetUp: Boolean,
    themeUiState: ThemeUiState,
    onNavigateBack: () -> Unit,
    onContinueSetupButton: () -> Unit,
    onSetUseDeviceTheme: (Boolean) -> Unit,
    onChooseLightTheme: (String) -> Unit,
    onChooseDarkTheme: (String) -> Unit
) {
    val settingsCategories = SettingsCategories(CurrAppTheme)
    val appearanceSettingsCategories = listOf(
        settingsCategories.colorTheme
    )
    val sheetState = rememberModalBottomSheetState()
    var showSettingsCategory by remember { mutableStateOf<Int?>(null) }
    val backgroundColor by animateColorAsState(
        targetValue = GlanceTheme.background,
        label = "background color"
    )

    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 8.dp,
                    bottom = 8.dp
                )
        ) {
            if (isAppSetUp && WindowTypeIsCompact) {
                GlassSurfaceNavigationButton(
                    textRes = settingsCategories.appearance.stringRes,
                    imageRes = settingsCategories.appearance.iconRes,
                    showRightIconInsteadOfLeft = false,
                    filledWidths = FilledWidthByScreenType(.96f),
                    onClick = onNavigateBack
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            appearanceSettingsCategories.forEach { category ->
                GlassSurfaceNavigationButton(
                    textRes = category.stringRes,
                    imageRes = category.iconRes,
                    showRightIconInsteadOfLeft = true,
                    filledWidths = FilledWidthByScreenType(),
                    onClick = {
                        showSettingsCategory = category.stringRes
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            if (!isAppSetUp) {
                PrimaryButton(
                    onClick = onContinueSetupButton,
                    text = stringResource(R.string._continue)
                )
            }
        }
        GlanceBottomSheet(
            visible = showSettingsCategory != null,
            sheetState = sheetState,
            onDismissRequest = { showSettingsCategory = null },
            backgroundColor = backgroundColor
        ) {
            if (showSettingsCategory == settingsCategories.colorTheme.stringRes) {
                ThemePicker(
                    onChooseLightTheme = onChooseLightTheme,
                    onChooseDarkTheme = onChooseDarkTheme,
                    onSetUseDeviceTheme = onSetUseDeviceTheme,
                    themeUiState = themeUiState
                )
            }
        }
    }
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun AppearanceScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    isSetupProgressTopBarVisible: Boolean = false,
    isBottomBarVisible: Boolean = false,
    themeUiState: ThemeUiState = ThemeUiState(
        useDeviceTheme = false,
        chosenLightTheme = AppTheme.LightDefault.name,
        chosenDarkTheme = AppTheme.DarkDefault.name,
        lastChosenTheme = AppTheme.LightDefault.name
    )
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible
    ) {
        AppearanceScreen(
            isAppSetUp = isAppSetUp,
            themeUiState = themeUiState,
            onNavigateBack = {},
            onContinueSetupButton = {},
            onSetUseDeviceTheme = {},
            onChooseLightTheme = {},
            onChooseDarkTheme = {}
        )
    }
}
