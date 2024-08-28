package com.ataglance.walletglance.navigation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ataglance.walletglance.core.utils.currentScreenIsNoneOf
import com.ataglance.walletglance.core.utils.fromMainScreen
import com.ataglance.walletglance.core.utils.simpleName
import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import com.ataglance.walletglance.navigation.data.repository.NavigationRepository
import com.ataglance.walletglance.navigation.domain.mapper.toBottomBarNavigationButtonList
import com.ataglance.walletglance.navigation.domain.mapper.toDefaultNavigationButtonEntityList
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButtons
import com.ataglance.walletglance.navigation.domain.model.MainScreens
import com.ataglance.walletglance.settings.navigation.SettingsScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val navigationRepository: NavigationRepository
) : ViewModel() {

    private val _bottomBarNavigationButtons: MutableStateFlow<List<BottomBarNavigationButtons>> =
        MutableStateFlow(
            listOf(
                BottomBarNavigationButtons.Home,
                BottomBarNavigationButtons.Records,
                BottomBarNavigationButtons.CategoryStatistics,
                BottomBarNavigationButtons.Budgets,
                BottomBarNavigationButtons.Settings
            )
        )
    val bottomBarNavigationButtons = _bottomBarNavigationButtons.asStateFlow()

    fun fetchBottomBarNavigationButtons() {
        viewModelScope.launch {
            navigationRepository.getNavigationButtonsSorted().collect { buttons ->
                if (buttons.isNotEmpty()) {
                    _bottomBarNavigationButtons.update {
                        buttons.toBottomBarNavigationButtonList()
                    }
                } else {
                    val defaultNavigationButtonList = getDefaultNavigationButtonList()

                    navigationRepository.upsertNavigationButtons(defaultNavigationButtonList)
                    _bottomBarNavigationButtons.update {
                        defaultNavigationButtonList.toBottomBarNavigationButtonList()
                    }
                }
            }
        }
    }

    private fun getDefaultNavigationButtonList(): List<NavigationButtonEntity> {
        return listOf(
            BottomBarNavigationButtons.Home,
            BottomBarNavigationButtons.Records,
            BottomBarNavigationButtons.CategoryStatistics,
            BottomBarNavigationButtons.Budgets,
            BottomBarNavigationButtons.Settings
        ).toDefaultNavigationButtonEntityList()
    }

    fun saveBottomBarNavigationButtons(navigationButtonEntityList: List<NavigationButtonEntity>) {
        viewModelScope.launch {
            navigationRepository.upsertNavigationButtons(navigationButtonEntityList)
            _bottomBarNavigationButtons.update {
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
        listOf(
            MainScreens.Home::class.simpleName(),
            MainScreens.Records::class.simpleName(),
            MainScreens.MakeRecord::class.simpleName(),
            MainScreens.CategoryStatistics(0)::class.simpleName(),
            MainScreens.Budgets::class.simpleName(),
            MainScreens.Settings::class.simpleName()
        ).forEach { screenRoute ->
            if (currentRoute == screenRoute) {
                return true
            } else if (nextRoute == screenRoute) {
                return false
            }
        }
        return true
    }


    fun shouldDisplaySetupProgressTopBar(
        mainStartDestination: MainScreens,
        navBackStackEntry: NavBackStackEntry?
    ): Boolean {
        return mainStartDestination != MainScreens.Home && navBackStackEntry.currentScreenIsNoneOf(
            SettingsScreens.SettingsHome, MainScreens.FinishSetup
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