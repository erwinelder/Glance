package com.ataglance.walletglance.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ataglance.walletglance.core.navigation.MainScreens
import com.ataglance.walletglance.core.utils.currentScreenIsNoneOf
import com.ataglance.walletglance.core.utils.fromMainScreen
import com.ataglance.walletglance.core.utils.simpleName
import com.ataglance.walletglance.makingRecord.domain.MakeRecordStatus
import com.ataglance.walletglance.settings.navigation.SettingsScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavigationViewModel : ViewModel() {

    private val _moveScreenTowardsLeft: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val moveScreenTowardsLeft: StateFlow<Boolean> = _moveScreenTowardsLeft.asStateFlow()

    fun setMoveScreenTowardsLeft(value: Boolean) {
        _moveScreenTowardsLeft.update { value }
    }

    private fun changeMoveScreenTowardsLeft(currentScreen: MainScreens, nextScreen: MainScreens) {
        setMoveScreenTowardsLeft(needToMoveScreenTowardsLeft(currentScreen, nextScreen))
    }

    private fun needToMoveScreenTowardsLeft(
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
        changeMoveScreenTowardsLeft(navBackStackEntry.fromMainScreen(), screenNavigateTo)
        navController.navigate(screenNavigateTo) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    fun onMakeRecordButtonClick(
        navController: NavController,
        recordNum: Int
    ) {
        setMoveScreenTowardsLeft(true)
        navController.navigate(
            MainScreens.MakeRecord(
                status = MakeRecordStatus.Create.name,
                recordNum = recordNum
            )
        ) {
            launchSingleTop = true
        }
    }

}
