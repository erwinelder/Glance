package com.ataglance.walletglance.personalization.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.CurrAppTheme
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton
import com.ataglance.walletglance.personalization.domain.model.CheckedWidget
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.presentation.components.NavigationButtonsSettingsBottomSheet
import com.ataglance.walletglance.personalization.presentation.components.ThemeSettingsBottomSheet
import com.ataglance.walletglance.personalization.presentation.components.WidgetsSettingsBottomSheet
import com.ataglance.walletglance.personalization.presentation.viewmodel.EditNavigationButtonsViewModel
import com.ataglance.walletglance.personalization.presentation.viewmodel.EditNavigationButtonsViewModelFactory
import com.ataglance.walletglance.personalization.presentation.viewmodel.EditWidgetsViewModel
import com.ataglance.walletglance.personalization.presentation.viewmodel.EditWidgetsViewModelFactory
import com.ataglance.walletglance.settings.domain.SettingsCategories
import com.ataglance.walletglance.settings.domain.ThemeUiState
import com.ataglance.walletglance.settings.presentation.components.OpenSettingsCategoryButton
import com.ataglance.walletglance.settings.presentation.screenContainers.SettingsCategoryScreenContainer

@Composable
fun AppearanceScreen(
    isAppSetUp: Boolean,
    themeUiState: ThemeUiState,
    onNavigateBack: () -> Unit,
    onSetUseDeviceTheme: (Boolean) -> Unit,
    onChooseLightTheme: (String) -> Unit,
    onChooseDarkTheme: (String) -> Unit,
    initialNavigationButtonList: List<BottomBarNavigationButton>,
    onSaveNavigationButtons: (List<BottomBarNavigationButton>) -> Unit,
    initialWidgetNamesList: List<WidgetName>,
    onSaveWidgetNames: (List<CheckedWidget>) -> Unit,
    onContinueSetupButton: () -> Unit
) {
    val settingsCategories = SettingsCategories(CurrAppTheme)
    val appearanceSettingsCategories = listOf(
        settingsCategories.colorTheme,
        settingsCategories.widgets,
        settingsCategories.navigationButtons
    )

    var showThemeSettingsBottomSheet by remember { mutableStateOf(false) }

    var showWidgetsSettingsBottomSheet by remember { mutableStateOf(false) }
    val editWidgetsViewModel = viewModel<EditWidgetsViewModel>(
        factory = EditWidgetsViewModelFactory(widgetList = initialWidgetNamesList)
    )
    val widgetList by editWidgetsViewModel.widgetList.collectAsStateWithLifecycle()

    var showNavigationButtonsSettingsBottomSheet by remember { mutableStateOf(false) }
    val editNavigationButtonsViewModel = viewModel<EditNavigationButtonsViewModel>(
        factory = EditNavigationButtonsViewModelFactory(
            initialNavigationButtonList = initialNavigationButtonList
        )
    )
    val navigationButtonList by editNavigationButtonsViewModel.navigationButtonList
        .collectAsStateWithLifecycle()


    Box {
        SettingsCategoryScreenContainer(
            thisCategory = settingsCategories.appearance,
            onNavigateBack = onNavigateBack.takeIf { isAppSetUp },
            title = stringResource(R.string.appearance_settings_category_title),
            mainScreenContentBlock = {
                appearanceSettingsCategories.forEach { category ->
                    OpenSettingsCategoryButton(category) {
                        when (category) {
                            settingsCategories.colorTheme -> showThemeSettingsBottomSheet = true
                            settingsCategories.widgets -> showWidgetsSettingsBottomSheet = true
                            settingsCategories.navigationButtons ->
                                showNavigationButtonsSettingsBottomSheet = true
                        }
                    }
                }
            },
            bottomBlock = if (!isAppSetUp) {{
                PrimaryButton(
                    onClick = onContinueSetupButton,
                    text = stringResource(R.string._continue)
                )
            }} else null
        )
        ThemeSettingsBottomSheet(
            visible = showThemeSettingsBottomSheet,
            onDismissRequest = { showThemeSettingsBottomSheet = false },
            themeUiState = themeUiState,
            onSetUseDeviceTheme = onSetUseDeviceTheme,
            onChooseLightTheme = onChooseLightTheme,
            onChooseDarkTheme = onChooseDarkTheme
        )
        WidgetsSettingsBottomSheet(
            visible = showWidgetsSettingsBottomSheet,
            onDismissRequest = {
                onSaveWidgetNames(editWidgetsViewModel.getWidgetList())
                showWidgetsSettingsBottomSheet = false
            },
            widgetList = widgetList,
            onWidgetCheckedStateChange = editWidgetsViewModel::changeWidgetCheckState,
            onMoveWidgets = editWidgetsViewModel::moveWidgets
        )
        NavigationButtonsSettingsBottomSheet(
            visible = showNavigationButtonsSettingsBottomSheet,
            onDismissRequest = {
                onSaveNavigationButtons(editNavigationButtonsViewModel.getNavigationButtonList())
                showNavigationButtonsSettingsBottomSheet = false
            },
            navigationButtonList = navigationButtonList,
            onMoveButtons = editNavigationButtonsViewModel::moveButtons
        )
    }
}


@Preview(device = Devices.PIXEL_7_PRO, locale = "en")
@Composable
fun AppearanceScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = false,
    isBottomBarVisible: Boolean = false,
    themeUiState: ThemeUiState = ThemeUiState(
        useDeviceTheme = false,
        chosenLightTheme = AppTheme.LightDefault.name,
        chosenDarkTheme = AppTheme.DarkDefault.name,
        lastChosenTheme = AppTheme.LightDefault.name
    ),
    widgetNamesList: List<WidgetName> = listOf(
        WidgetName.ChosenBudgets,
        WidgetName.TotalForPeriod,
        WidgetName.TopExpenseCategories,
        WidgetName.RecentRecords
    )
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isBottomBarVisible = isBottomBarVisible
    ) {
        AppearanceScreen(
            isAppSetUp = isAppSetUp,
            themeUiState = themeUiState,
            onNavigateBack = {},
            onSetUseDeviceTheme = {},
            onChooseLightTheme = {},
            onChooseDarkTheme = {},
            initialNavigationButtonList = listOf(
                BottomBarNavigationButton.Home,
                BottomBarNavigationButton.Records,
                BottomBarNavigationButton.CategoryStatistics,
                BottomBarNavigationButton.Budgets,
                BottomBarNavigationButton.Settings
            ),
            onSaveNavigationButtons = {},
            initialWidgetNamesList = widgetNamesList,
            onSaveWidgetNames = {},
            onContinueSetupButton = {}
        )
    }
}
