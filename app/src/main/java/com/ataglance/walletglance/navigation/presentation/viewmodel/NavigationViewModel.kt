package com.ataglance.walletglance.navigation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ataglance.walletglance.auth.domain.model.AuthResultSuccessScreenType
import com.ataglance.walletglance.auth.domain.model.SignInCase
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton
import com.ataglance.walletglance.navigation.domain.usecase.GetNavigationButtonsUseCase
import com.ataglance.walletglance.navigation.domain.utils.currentScreenIsAnyOf
import com.ataglance.walletglance.navigation.domain.utils.fromMainScreen
import com.ataglance.walletglance.navigation.domain.utils.simpleName
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val getNavigationButtonsUseCase: GetNavigationButtonsUseCase
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

    private fun fetchBottomBarNavigationButtons() {
        viewModelScope.launch {
            getNavigationButtonsUseCase.getFlow().collect { buttons ->
                _navigationButtonList.update { buttons }
            }
        }
    }


    private val _moveScreensTowardsLeft: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val moveScreensTowardsLeft: StateFlow<Boolean> = _moveScreensTowardsLeft.asStateFlow()

    private fun setMoveScreensTowardsLeft(value: Boolean) {
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
        navigationButtonList.value
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


    fun shouldDisplayBottomNavigationBar(
        isSetUp: Boolean,
        navBackStackEntry: NavBackStackEntry?
    ): Boolean {
        return isSetUp && navBackStackEntry.currentScreenIsAnyOf(
            MainScreens.Home, MainScreens.Records, MainScreens.CategoryStatistics(),
            MainScreens.Budgets, SettingsScreens.SettingsHome
        )
    }


    fun popBackStackToHomeScreen(navController: NavController) {
        navController.popBackStack(MainScreens.Home, false)
    }

    fun navigateToScreenPoppingToStartDestination(
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

    fun navigateToSignInScreen(
        navController: NavController,
        signInCase: SignInCase
    ) {
        navigateToScreen(navController, AuthScreens.SignIn(signInCase.name))
    }

    fun popBackStackAndNavigateToResultSuccessScreen(
        navController: NavController,
        screenType: AuthResultSuccessScreenType
    ) {
        navController.popBackStack()
        navigateToScreen(navController, AuthScreens.ResultSuccess(screenType.name))
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