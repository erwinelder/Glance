package com.ataglance.walletglance.navigation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ataglance.walletglance.core.navigation.MainScreens
import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import com.ataglance.walletglance.navigation.data.repository.NavigationRepository
import com.ataglance.walletglance.navigation.domain.mapper.toBottomBarNavigationButtonList
import com.ataglance.walletglance.navigation.domain.mapper.toDefaultNavigationButtonEntityList
import com.ataglance.walletglance.navigation.domain.mapper.toNavigationButtonEntityList
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton
import com.ataglance.walletglance.navigation.utils.currentScreenIsNoneOf
import com.ataglance.walletglance.navigation.utils.currentScreenIsOneOf
import com.ataglance.walletglance.navigation.utils.fromMainScreen
import com.ataglance.walletglance.navigation.utils.simpleName
import com.ataglance.walletglance.settings.navigation.SettingsScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val navigationRepository: NavigationRepository
) : ViewModel() {

    private val _navigationButtonList: MutableStateFlow<List<BottomBarNavigationButton>> =
        MutableStateFlow(
            listOf(
                BottomBarNavigationButton.Home,
                BottomBarNavigationButton.Records,
                BottomBarNavigationButton.CategoryStatistics,
                BottomBarNavigationButton.Budgets,
                BottomBarNavigationButton.Settings
            )
        )
    val navigationButtonList = _navigationButtonList.asStateFlow()

    fun fetchBottomBarNavigationButtons() {
        viewModelScope.launch {
            navigationRepository.getNavigationButtonsSorted().collect { buttons ->
                if (buttons.isNotEmpty()) {
                    _navigationButtonList.update {
                        buttons.toBottomBarNavigationButtonList()
                    }
                } else {
                    val defaultNavigationButtonList = getDefaultNavigationButtonList()

                    navigationRepository.upsertNavigationButtons(defaultNavigationButtonList)
                    _navigationButtonList.update {
                        defaultNavigationButtonList.toBottomBarNavigationButtonList()
                    }
                }
            }
        }
    }

    private fun getDefaultNavigationButtonList(): List<NavigationButtonEntity> {
        return listOf(
            BottomBarNavigationButton.Home,
            BottomBarNavigationButton.Records,
            BottomBarNavigationButton.CategoryStatistics,
            BottomBarNavigationButton.Budgets,
            BottomBarNavigationButton.Settings
        ).toDefaultNavigationButtonEntityList()
    }

    fun saveBottomBarNavigationButtons(navigationButtonList: List<BottomBarNavigationButton>) {
        val navigationButtonEntityList = navigationButtonList.toNavigationButtonEntityList()

        viewModelScope.launch {
            navigationRepository.upsertNavigationButtons(navigationButtonEntityList)
            _navigationButtonList.update {
                navigationButtonEntityList.toBottomBarNavigationButtonList()
            }
        }
    }


    private val _moveScreensTowardsLeft: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val moveScreensTowardsLeft: StateFlow<Boolean> = _moveScreensTowardsLeft.asStateFlow()

    private fun setMoveScreensTowardsLeft(value: Boolean) {
        _moveScreensTowardsLeft.update { value }
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
        navigationButtonList.value
            .map { it::class.simpleName() }
            .forEach { screenRoute ->
                if (currentRoute == screenRoute) {
                    return true
                } else if (nextRoute == screenRoute) {
                    return false
                }
            }
        return true
    }


    fun shouldDisplaySetupProgressTopBar(
        isSetUp: Boolean,
        navBackStackEntry: NavBackStackEntry?
    ): Boolean {
        return !isSetUp && navBackStackEntry.currentScreenIsNoneOf(
            SettingsScreens.Start, MainScreens.FinishSetup
        )
    }


    fun shouldDisplayBottomNavigationBar(
        isSetUp: Boolean,
        navBackStackEntry: NavBackStackEntry?
    ): Boolean {
        return isSetUp && navBackStackEntry.currentScreenIsOneOf(
            MainScreens.Home, MainScreens.Records, MainScreens.CategoryStatistics(0),
            MainScreens.Budgets, SettingsScreens.SettingsHome, SettingsScreens.Language
        )
    }


    fun navigateToScreenAndPopUp(
        navController: NavController,
        navBackStackEntry: NavBackStackEntry?,
        screenNavigateTo: MainScreens
    ) {
        changeMoveScreensTowardsLeft(navBackStackEntry.fromMainScreen(), screenNavigateTo)
        navController.navigate(screenNavigateTo) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    fun navigateToScreenMovingTowardsLeft(
        navController: NavController,
        screen: Any
    ) {
        setMoveScreensTowardsLeft(true)
        navController.navigate(screen) {
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

}