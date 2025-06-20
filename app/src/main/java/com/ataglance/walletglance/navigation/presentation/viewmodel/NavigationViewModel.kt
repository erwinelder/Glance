package com.ataglance.walletglance.navigation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.navigation.domain.usecase.GetNavigationButtonScreensUseCase
import com.ataglance.walletglance.navigation.domain.utils.anyScreenInHierarchyIs
import com.ataglance.walletglance.navigation.domain.utils.currentScreenIs
import com.ataglance.walletglance.navigation.domain.utils.currentScreenIsAnyOf
import com.ataglance.walletglance.navigation.domain.utils.fromMainScreen
import com.ataglance.walletglance.navigation.domain.utils.simpleName
import com.ataglance.walletglance.navigation.mapper.toAppScreenEnum
import com.ataglance.walletglance.navigation.mapper.toBottomBarNavButtonState
import com.ataglance.walletglance.navigation.presentation.model.BottomNavBarButtonState
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val getNavigationButtonScreensUseCase: GetNavigationButtonScreensUseCase
) : ViewModel() {

    private val _isBottomBarVisible = MutableStateFlow(false)
    val isBottomBarVisible = _isBottomBarVisible.asStateFlow()

    fun updateBottomBarVisibility(isSetUp: Boolean, navBackStackEntry: NavBackStackEntry?) {
        val isVisible = isSetUp && navBackStackEntry.currentScreenIsAnyOf(
            MainScreens.Home, MainScreens.Records, MainScreens.CategoryStatistics(),
            MainScreens.Budgets, SettingsScreens.SettingsHome
        )
        _isBottomBarVisible.update { isVisible }
    }

    fun setBottomBarVisibility(isVisible: Boolean) {
        _isBottomBarVisible.update { isVisible }
    }


    private val _isBottomBarExpanded = MutableStateFlow(false)
    val isBottomBarExpanded = _isBottomBarExpanded.asStateFlow()

    fun toggleBottomBarExpanded() {
        _isBottomBarExpanded.update { !it }
        primaryNavigationButtons.value.map { button ->
            button.takeIf { it is BottomNavBarButtonState.Other }?.toggleActive() ?: button
        }
    }


    private val _navigationButtons = MutableStateFlow<List<BottomNavBarButtonState>>(
        BottomNavBarButtonState.asDefaultList()
    )

    val primaryNavigationButtons: StateFlow<List<BottomNavBarButtonState>> = _navigationButtons
        .map { buttons ->
            if (buttons.size <= 5) {
                buttons
            } else {
                buttons.take(4).toMutableList().apply {
                    this.add(BottomNavBarButtonState.Other(isActive = false))
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val secondaryNavigationButtons: StateFlow<List<BottomNavBarButtonState>> = _navigationButtons
        .map { buttons ->
            if (buttons.size <= 5) {
                emptyList()
            } else {
                buttons.takeLast(buttons.size - 4).reversed()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private fun fetchBottomBarNavigationButtons() {
        viewModelScope.launch {
            getNavigationButtonScreensUseCase.getFlow().collect { screens ->
                val activeScreen = _navigationButtons.value
                    .find { it.isActive && it !is BottomNavBarButtonState.Other }
                    ?.toAppScreenEnum()
                val buttons = screens.map {
                    it.toBottomBarNavButtonState(activeScreen = activeScreen)
                }
                _navigationButtons.update { buttons }
            }
        }
    }

    fun updateBottomBarActiveButton(navBackStackEntry: NavBackStackEntry?) {
        _navigationButtons.update { buttons ->
            buttons.map { button ->
                button.updateActive(
                    isActive = navBackStackEntry.anyScreenInHierarchyIs(screen = button.screen)
                )
            }
        }
    }

    private var timerIsUp = true

    fun onNavButtonClick(
        button: BottomNavBarButtonState,
        navController: NavController,
        navBackStackEntry: NavBackStackEntry?
    ) {
        if (button is BottomNavBarButtonState.Other) {
            toggleBottomBarExpanded()
            return
        }

        if (navBackStackEntry.currentScreenIs(button.screen) || !timerIsUp) return

        viewModelScope.launch {
            timerIsUp = false
            delay(500)
            timerIsUp = true
        }

        navigateToScreenPoppingToStartDestination(
            navController = navController,
            navBackStackEntry = navBackStackEntry,
            screenToNavigateTo = button.screen
        )

        if (_isBottomBarExpanded.value) {
            _isBottomBarExpanded.update { false }
        }
    }


    private val _moveScreensTowardsLeft: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val moveScreensTowardsLeft: StateFlow<Boolean> = _moveScreensTowardsLeft.asStateFlow()

    fun setMoveScreensTowardsLeft(value: Boolean) {
        _moveScreensTowardsLeft.update { value }
    }


    init {
        fetchBottomBarNavigationButtons()
    }


    private fun changeMoveScreensTowardsLeft(currentScreen: MainScreens, nextScreen: MainScreens) {
        setMoveScreensTowardsLeft(needToMoveScreensTowardsLeft(currentScreen, nextScreen))
    }

    private fun needToMoveScreensTowardsLeft(
        currentScreen: MainScreens,
        nextScreen: MainScreens
    ): Boolean {
        val currentRoute = currentScreen::class.simpleName()
        val nextRoute = nextScreen::class.simpleName()
        _navigationButtons.value
            .map { it.screen::class.simpleName() }
            .forEach { screenRoute ->
                if (currentRoute == screenRoute) {
                    return true
                } else if (nextRoute == screenRoute) {
                    return false
                }
            }
        return true
    }


    fun popBackStackToHomeScreen(navController: NavController) {
        navController.popBackStack(MainScreens.Home, false)
    }

    fun navigateToScreenPoppingToStartDestination(
        screenToNavigateTo: MainScreens,
        navController: NavController,
        navBackStackEntry: NavBackStackEntry?
    ) {
        changeMoveScreensTowardsLeft(navBackStackEntry.fromMainScreen(), screenToNavigateTo)
        navController.navigate(screenToNavigateTo) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    fun navigateToScreenPoppingToStartDestination(
        navController: NavController,
        screenToNavigateTo: Any
    ) {
        setMoveScreensTowardsLeft(false)
        navController.navigate(screenToNavigateTo) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    fun navigateToScreen(
        navController: NavController,
        screen: Any
    ) {
        navController.navigate(screen) {
            launchSingleTop = true
        }
    }

    fun popBackStackAndNavigate(
        navController: NavController,
        screen: Any
    ) {
        navController.popBackStack()
        navigateToScreen(navController, screen)
    }

    fun navigateToScreenMovingTowardsLeft(
        navController: NavController,
        screen: Any
    ) {
        setMoveScreensTowardsLeft(true)
        navigateToScreen(navController, screen)
    }

    fun navigateToScreenMovingTowardsRight(
        navController: NavController,
        screen: Any
    ) {
        setMoveScreensTowardsLeft(false)
        navigateToScreen(navController, screen)
    }

    fun <N : Any> navigateAndPopUpTo(
        navController: NavController,
        screenToNavigateTo: N,
        inclusive: Boolean = true
    ) {
        navController.navigate(screenToNavigateTo) {
            launchSingleTop = true
            popUpTo(0) {
                this.inclusive = inclusive
            }
        }
    }

}