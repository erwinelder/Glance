package com.ataglance.walletglance.personalization.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.utils.moveItems
import com.ataglance.walletglance.navigation.domain.usecase.GetNavigationButtonScreensUseCase
import com.ataglance.walletglance.navigation.domain.usecase.SaveNavigationButtonsUseCase
import com.ataglance.walletglance.navigation.mapper.toAppScreenEnum
import com.ataglance.walletglance.navigation.mapper.toBottomBarNavButtonState
import com.ataglance.walletglance.navigation.presentation.model.BottomNavBarButtonState
import com.ataglance.walletglance.personalization.domain.model.CheckedWidget
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.domain.usecase.theme.ChangeAppLookPreferencesUseCase
import com.ataglance.walletglance.personalization.domain.usecase.theme.GetAppThemeConfigurationUseCase
import com.ataglance.walletglance.personalization.domain.usecase.widgets.GetWidgetsUseCase
import com.ataglance.walletglance.personalization.domain.usecase.widgets.SaveWidgetsUseCase
import com.ataglance.walletglance.settings.domain.model.AppThemeConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonalizationViewModel(
    private val getAppThemeConfigurationUseCase: GetAppThemeConfigurationUseCase,
    private val changeAppLookPreferencesUseCase: ChangeAppLookPreferencesUseCase,
    private val saveWidgetsUseCase: SaveWidgetsUseCase,
    private val getWidgetsUseCase: GetWidgetsUseCase,
    private val saveNavigationButtonsUseCase: SaveNavigationButtonsUseCase,
    private val getNavigationButtonScreensUseCase: GetNavigationButtonScreensUseCase,
    private val userContext: UserContext
) : ViewModel() {

    private val _showThemeSettings = MutableStateFlow(false)
    val showThemeSettings = _showThemeSettings.asStateFlow()

    fun showThemeSettings() = _showThemeSettings.update { true }
    fun hideThemeSettings() = _showThemeSettings.update { false }


    private val _appThemeConfiguration = MutableStateFlow(AppThemeConfiguration())
    val appThemeConfiguration = _appThemeConfiguration.asStateFlow()

    private fun fetchAppThemeConfiguration() {
        viewModelScope.launch {
            getAppThemeConfigurationUseCase.getFlow().collect { themeConfiguration ->
                _appThemeConfiguration.update { themeConfiguration }
            }
        }
    }

    fun setLightTheme(theme: AppTheme) {
        viewModelScope.launch {
            changeAppLookPreferencesUseCase.saveLightThemePreference(theme = theme)
        }
    }

    fun setDarkTheme(theme: AppTheme) {
        viewModelScope.launch {
            changeAppLookPreferencesUseCase.saveDarkThemePreference(theme = theme)
        }
    }

    fun setUseDeviceTheme(value: Boolean) {
        viewModelScope.launch {
            changeAppLookPreferencesUseCase.saveUseDeviceThemePreference(value = value)
        }
    }


    private val _showWidgetsSettings = MutableStateFlow(false)
    val showWidgetsSettings = _showWidgetsSettings.asStateFlow()

    fun showWidgetsSettings() = _showWidgetsSettings.update { true }
    private fun hideWidgetsSettings() = _showWidgetsSettings.update { false }


    private val _widgets = MutableStateFlow<List<CheckedWidget>>(emptyList())
    val widgets = _widgets.asStateFlow()

    private fun fetchWidgets() {
        viewModelScope.launch {
            getWidgetsUseCase.getAsFlow().collect { widgets ->
                _widgets.update {
                    widgets.map { widgetName ->
                        CheckedWidget(widgetName, widgetName in widgets)
                    }
                }
            }
        }
    }

    fun changeWidgetCheckState(widgetName: WidgetName, isChecked: Boolean) {
        _widgets.update { widgetList ->
            widgetList.map { widget ->
                widget.takeIf { it.name != widgetName } ?: widget.copy(isChecked = isChecked)
            }
        }
    }

    fun moveWidgets(fromIndex: Int, toIndex: Int) {
        _widgets.update {
            it.moveItems(fromIndex, toIndex)
        }
    }

    private suspend fun saveWidgets() {
        val widgets = widgets.value.filter { it.isChecked }.map { it.name }
        saveWidgetsUseCase.saveAndDeleteRest(widgets = widgets)
    }

    fun saveWidgetsAndCloseSettings() {
        viewModelScope.launch {
            saveWidgets()
            hideWidgetsSettings()
        }
    }


    private val _showNavButtonsSettings = MutableStateFlow(false)
    val showNavButtonsSettings = _showNavButtonsSettings.asStateFlow()

    fun showNavButtonsSettings() = _showNavButtonsSettings.update { true }
    private fun hideNavButtonsSettings() = _showNavButtonsSettings.update { false }


    private val _navButtons = MutableStateFlow<List<BottomNavBarButtonState>>(emptyList())
    val navButtons = _navButtons.asStateFlow()

    private fun fetchNavButtons() {
        viewModelScope.launch {
            getNavigationButtonScreensUseCase.getFlow().collect { screens ->
                val buttons = screens.map { it.toBottomBarNavButtonState() }
                _navButtons.update {
                    buttons.takeIf { it.size > 1 }?.subList(1, buttons.lastIndex) ?: buttons
                }
            }
        }
    }

    fun swapNavButtons(fromIndex: Int, toIndex: Int) {
        _navButtons.update {
            it.takeIf { it.size >= 2 }?.moveItems(fromIndex, toIndex) ?: it
        }
    }

    private suspend fun saveNavButtons() {
        val screens = navButtons.value.mapNotNull { it.toAppScreenEnum() }
        saveNavigationButtonsUseCase.execute(screens = screens)
    }

    fun saveNavButtonsAndCloseSettings() {
        viewModelScope.launch {
            saveNavButtons()
            hideNavButtonsSettings()
        }
    }


    fun isUserSignedIn(): Boolean {
        return userContext.isSignedIn()
    }


    init {
        fetchAppThemeConfiguration()
        fetchWidgets()
        fetchNavButtons()
    }

}