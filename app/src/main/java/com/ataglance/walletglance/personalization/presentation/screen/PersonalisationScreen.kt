package com.ataglance.walletglance.personalization.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.utils.takeComposableIf
import com.ataglance.walletglance.personalization.presentation.components.NavigationButtonsSettingsBottomSheet
import com.ataglance.walletglance.personalization.presentation.components.ThemeSettingsBottomSheet
import com.ataglance.walletglance.personalization.presentation.components.WidgetsSettingsBottomSheet
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalisationViewModel
import com.ataglance.walletglance.settings.presentation.components.OpenSettingsCategoryButton
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory
import com.ataglance.walletglance.settings.presentation.screenContainers.SettingsCategoryScreenContainer
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PersonalisationScreen(
    isAppSetUp: Boolean,
    onNavigateBack: () -> Unit,
    onContinueSetupButton: () -> Unit
) {
    val personalisationViewModel = koinViewModel<PersonalisationViewModel>()

    val showThemeSettings by personalisationViewModel.showThemeSettings.collectAsStateWithLifecycle()
    val appThemeConfiguration by personalisationViewModel.appThemeConfiguration.collectAsStateWithLifecycle()

    val showWidgetSettings by personalisationViewModel.showWidgetsSettings.collectAsStateWithLifecycle()
    val widgets by personalisationViewModel.widgets.collectAsStateWithLifecycle()

    val showNavButtonsSettings by personalisationViewModel.showNavButtonsSettings.collectAsStateWithLifecycle()
    val navigationButtons by personalisationViewModel.navButtons.collectAsStateWithLifecycle()

    Box {
        SettingsCategoryScreenContainer(
            thisCategory = SettingsCategory.Appearance(CurrAppTheme),
            onNavigateBack = onNavigateBack.takeIf { isAppSetUp },
            title = stringResource(R.string.appearance_settings_category_title),
            mainScreenContentBlock = {
                OpenSettingsCategoryButton(
                    category = SettingsCategory.ColorTheme(CurrAppTheme),
                    onClick = personalisationViewModel::showThemeSettings
                )
                OpenSettingsCategoryButton(
                    category = SettingsCategory.Widgets(CurrAppTheme),
                    onClick = personalisationViewModel::showWidgetsSettings
                )
                OpenSettingsCategoryButton(
                    category = SettingsCategory.NavigationButtons(CurrAppTheme),
                    onClick = personalisationViewModel::showNavButtonsSettings
                )
            },
            bottomBlock = takeComposableIf(!isAppSetUp) {
                PrimaryButton(
                    onClick = onContinueSetupButton,
                    text = stringResource(R.string._continue)
                )
            }
        )
        ThemeSettingsBottomSheet(
            visible = showThemeSettings,
            onDismissRequest = personalisationViewModel::hideThemeSettings,
            appThemeConfiguration = appThemeConfiguration,
            onChooseLightTheme = personalisationViewModel::setLightTheme,
            onChooseDarkTheme = personalisationViewModel::setDarkTheme,
            onSetUseDeviceTheme = personalisationViewModel::setUseDeviceTheme
        )
        WidgetsSettingsBottomSheet(
            visible = showWidgetSettings,
            onDismissRequest = personalisationViewModel::saveWidgetsAndCloseSettings,
            widgets = widgets,
            onWidgetCheckedStateChange = personalisationViewModel::changeWidgetCheckState,
            onMoveWidgets = personalisationViewModel::moveWidgets
        )
        NavigationButtonsSettingsBottomSheet(
            visible = showNavButtonsSettings,
            onDismissRequest = personalisationViewModel::saveNavButtonsAndCloseSettings,
            navigationButtonList = navigationButtons,
            onMoveButtons = personalisationViewModel::swapNavButtons
        )
    }
}


@Preview(device = Devices.PIXEL_7_PRO, locale = "en")
@Composable
fun PersonalisationScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = false,
    isBottomBarVisible: Boolean = false
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isBottomBarVisible = isBottomBarVisible
    ) {
        PersonalisationScreen(
            isAppSetUp = isAppSetUp,
            onNavigateBack = {},
            onContinueSetupButton = {}
        )
    }
}
