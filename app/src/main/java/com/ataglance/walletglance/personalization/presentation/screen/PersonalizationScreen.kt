package com.ataglance.walletglance.personalization.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.utils.takeComposableIf
import com.ataglance.walletglance.navigation.presentation.model.BottomNavBarButtonState
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.personalization.domain.model.CheckedWidget
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.presentation.components.NavigationButtonsSettingsBottomSheet
import com.ataglance.walletglance.personalization.presentation.components.ThemeSettingsBottomSheet
import com.ataglance.walletglance.personalization.presentation.components.WidgetsSettingsBottomSheet
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel
import com.ataglance.walletglance.settings.domain.model.AppThemeConfiguration
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import com.ataglance.walletglance.settings.presentation.component.OpenSettingsCategoryButton
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory
import com.ataglance.walletglance.settings.presentation.screenContainer.SettingsCategoryScreenContainer
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PersonalizationScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavController,
    navViewModel: NavigationViewModel,
    appConfiguration: AppConfiguration
) {
    val personalizationViewModel = koinViewModel<PersonalizationViewModel>()

    val showThemeSettings by personalizationViewModel.showThemeSettings.collectAsStateWithLifecycle()
    val appThemeConfiguration by personalizationViewModel.appThemeConfiguration.collectAsStateWithLifecycle()

    val showWidgetSettings by personalizationViewModel.showWidgetsSettings.collectAsStateWithLifecycle()
    val widgets by personalizationViewModel.widgets.collectAsStateWithLifecycle()

    val showNavButtonsSettings by personalizationViewModel.showNavButtonsSettings.collectAsStateWithLifecycle()
    val navigationButtons by personalizationViewModel.navButtons.collectAsStateWithLifecycle()

    PersonalizationScreen(
        screenPadding = screenPadding,
        isAppSetUp = appConfiguration.isSetUp,
        onNavigateBack = navController::popBackStack,

        showThemeSettings = showThemeSettings,
        onShowThemeSettings = personalizationViewModel::showThemeSettings,
        onHideThemeSettings = personalizationViewModel::hideThemeSettings,
        appThemeConfiguration = appThemeConfiguration,
        onChooseLightTheme = personalizationViewModel::setLightTheme,
        onChooseDarkTheme = personalizationViewModel::setDarkTheme,
        onSetUseDeviceTheme = personalizationViewModel::setUseDeviceTheme,

        showWidgetSettings = showWidgetSettings,
        onShowWidgetSettings = personalizationViewModel::showWidgetsSettings,
        onSaveWidgetsAndCloseSettings = personalizationViewModel::saveWidgetsAndCloseSettings,
        widgets = widgets,
        changeWidgetCheckState = personalizationViewModel::changeWidgetCheckState,
        onMoveWidgets = personalizationViewModel::moveWidgets,

        showNavButtonsSettings = showNavButtonsSettings,
        onShowNavButtonsSettings = personalizationViewModel::showNavButtonsSettings,
        onSaveNavButtonsAndCloseSettings = personalizationViewModel::saveNavButtonsAndCloseSettings,
        navigationButtons = navigationButtons,
        onMoveButtons = personalizationViewModel::swapNavButtons,

        onContinueSetupButton = {
            if (personalizationViewModel.isUserSignedIn()) {
                navViewModel.navigateToScreen(
                    navController = navController, screen = SettingsScreens.Accounts
                )
            } else {
                navViewModel.navigateToScreen(
                    navController = navController, screen = AuthScreens.SignIn()
                )
            }
        }
    )
}

@Composable
fun PersonalizationScreen(
    screenPadding: PaddingValues = PaddingValues(),
    isAppSetUp: Boolean,
    onNavigateBack: () -> Unit,

    showThemeSettings: Boolean,
    onShowThemeSettings: () -> Unit,
    onHideThemeSettings: () -> Unit,
    appThemeConfiguration: AppThemeConfiguration,
    onChooseLightTheme: (AppTheme) -> Unit,
    onChooseDarkTheme: (AppTheme) -> Unit,
    onSetUseDeviceTheme: (Boolean) -> Unit,

    showWidgetSettings: Boolean,
    onShowWidgetSettings: () -> Unit,
    onSaveWidgetsAndCloseSettings: () -> Unit,
    widgets: List<CheckedWidget>,
    changeWidgetCheckState: (WidgetName, Boolean) -> Unit,
    onMoveWidgets: (Int, Int) -> Unit,

    showNavButtonsSettings: Boolean,
    onShowNavButtonsSettings: () -> Unit,
    onSaveNavButtonsAndCloseSettings: () -> Unit,
    navigationButtons: List<BottomNavBarButtonState>,
    onMoveButtons: (Int, Int) -> Unit,

    onContinueSetupButton: () -> Unit
) {
    Box {
        SettingsCategoryScreenContainer(
            screenPadding = screenPadding,
            thisCategory = SettingsCategory.Appearance(CurrAppTheme),
            onNavigateBack = onNavigateBack.takeIf { isAppSetUp },
            title = stringResource(R.string.appearance_settings_category_title),
            mainScreenContent = {
                OpenSettingsCategoryButton(
                    category = SettingsCategory.ColorTheme(CurrAppTheme),
                    onClick = onShowThemeSettings
                )
                OpenSettingsCategoryButton(
                    category = SettingsCategory.Widgets(CurrAppTheme),
                    onClick = onShowWidgetSettings
                )
                OpenSettingsCategoryButton(
                    category = SettingsCategory.NavigationButtons(CurrAppTheme),
                    onClick = onShowNavButtonsSettings
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
            onDismissRequest = onHideThemeSettings,
            appThemeConfiguration = appThemeConfiguration,
            onChooseLightTheme = onChooseLightTheme,
            onChooseDarkTheme = onChooseDarkTheme,
            onSetUseDeviceTheme = onSetUseDeviceTheme
        )
        WidgetsSettingsBottomSheet(
            visible = showWidgetSettings,
            onDismissRequest = onSaveWidgetsAndCloseSettings,
            widgets = widgets,
            onWidgetCheckedStateChange = changeWidgetCheckState,
            onMoveWidgets = onMoveWidgets
        )
        NavigationButtonsSettingsBottomSheet(
            visible = showNavButtonsSettings,
            onDismissRequest = onSaveNavButtonsAndCloseSettings,
            navigationButtonList = navigationButtons,
            onMoveButtons = onMoveButtons
        )
    }
}


@Preview(device = Devices.PIXEL_7_PRO, locale = "en")
@Composable
fun PersonalizationScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        PersonalizationScreen(
            isAppSetUp = isAppSetUp,
            onNavigateBack = {},

            showThemeSettings = false,
            onShowThemeSettings = {},
            onHideThemeSettings = {},
            appThemeConfiguration = AppThemeConfiguration(),
            onChooseLightTheme = {},
            onChooseDarkTheme = {},
            onSetUseDeviceTheme = {},

            showWidgetSettings = false,
            onShowWidgetSettings = {},
            onSaveWidgetsAndCloseSettings = {},
            widgets = emptyList(),
            changeWidgetCheckState = { _, _ -> },
            onMoveWidgets = { _, _ -> },

            showNavButtonsSettings = false,
            onShowNavButtonsSettings = {},
            onSaveNavButtonsAndCloseSettings = {},
            navigationButtons = emptyList(),
            onMoveButtons = { _, _ -> },

            onContinueSetupButton = {}
        )
    }
}
