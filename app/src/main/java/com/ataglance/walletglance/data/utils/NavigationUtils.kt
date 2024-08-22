package com.ataglance.walletglance.data.utils

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import com.ataglance.walletglance.presentation.theme.navigation.screens.MainScreens
import com.ataglance.walletglance.presentation.theme.navigation.screens.SettingsScreens
import kotlin.reflect.KClass


fun NavDestination?.fromRoute(): String {
    return this?.route?.substringBefore('/')?.substringAfterLast('.') ?: ""
}


fun NavBackStackEntry?.fromRoute(): String {
    return this?.destination?.route
        ?.substringBefore('/')?.substringAfterLast('.') ?: ""
}


fun NavBackStackEntry?.fromMainScreen(): MainScreens {
    this.fromRoute().let {
        when (it) {
            MainScreens.Home::class.simpleName() -> return MainScreens.Home
            MainScreens.Records::class.simpleName() -> return MainScreens.Records
            MainScreens.CategoryStatistics::class.simpleName() ->
                return MainScreens.CategoryStatistics(0)
            else -> return MainScreens.Settings
        }
    }
}


fun NavDestination?.currentScreenIs(screen: Any): Boolean {
    return this?.fromRoute() == screen::class.simpleName()
}


fun NavBackStackEntry?.currentScreenIs(screen: Any): Boolean {
    return this?.fromRoute() == screen::class.simpleName() ||
            (this?.fromRoute() == SettingsScreens.SettingsHome::class.simpleName() &&
                    screen::class.simpleName() == MainScreens.Settings::class.simpleName())
}


fun KClass<out Any>.simpleName(): String? {
    return this.simpleName?.substringAfterLast('$')
}


fun needToMoveScreenTowardsLeft(currentScreen: MainScreens, nextScreen: MainScreens): Boolean {
    val currentRoute = currentScreen::class.simpleName()
    val nextRoute = nextScreen::class.simpleName()
    listOf(
        MainScreens.Home::class.simpleName(),
        MainScreens.Records::class.simpleName(),
        MainScreens.MakeRecord::class.simpleName(),
        MainScreens.CategoryStatistics(0)::class.simpleName(),
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
