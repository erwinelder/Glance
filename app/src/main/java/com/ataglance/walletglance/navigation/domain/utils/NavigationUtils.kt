package com.ataglance.walletglance.navigation.domain.utils

import androidx.annotation.StringRes
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.navigation.AccountsSettingsScreens
import com.ataglance.walletglance.category.domain.navigation.CategoriesSettingsScreens
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import kotlin.reflect.KClass


fun NavDestination?.fromRoute(): String {
    return this?.route?.substringBefore('/')?.substringAfterLast('.')?.substringBefore('?') ?: ""
}

fun NavBackStackEntry?.fromRoute(): String {
    return this?.destination?.route
        ?.substringBefore('/')?.substringAfterLast('.')?.substringBefore("?") ?: ""
}

fun NavBackStackEntry?.fromMainScreen(): MainScreens {
    this.fromRoute().let {
        when (it) {
            MainScreens.Home::class.simpleName() -> return MainScreens.Home
            MainScreens.Transactions::class.simpleName() -> return MainScreens.Transactions
            MainScreens.CategoryStatistics::class.simpleName() ->
                return MainScreens.CategoryStatistics()
            MainScreens.Budgets::class.simpleName() -> return MainScreens.Budgets
            else -> return MainScreens.Settings
        }
    }
}


fun NavDestination?.currentScreenIs(screen: Any): Boolean {
    return this?.fromRoute() == screen::class.simpleName()
}

fun NavBackStackEntry?.currentScreenIs(screen: Any): Boolean {
    val screenSimpleName = screen::class.simpleName()
    val fromRoute = this?.fromRoute()

    return fromRoute == screenSimpleName ||
            (fromRoute == SettingsScreens.SettingsHome::class.simpleName() &&
                    screenSimpleName == MainScreens.Settings::class.simpleName())
}

fun NavBackStackEntry?.currentScreenIsAnyOf(vararg screens: Any): Boolean {
    this ?: return false
    return screens.any { this.currentScreenIs(it) }
}

fun NavBackStackEntry?.anyScreenInHierarchyIs(screen: Any): Boolean {
    this ?: return false
    return this.destination.hierarchy.any { it.currentScreenIs(screen) }
}


@StringRes fun NavBackStackEntry?.getSetupProgressTopBarTitleRes(): Int {
    return when {
        this.currentScreenIs(SettingsScreens.Language) -> R.string.language
        this.currentScreenIs(SettingsScreens.Personalization) -> R.string.appearance
        this.currentScreenIs(AccountsSettingsScreens.EditAccounts) ||
                this.currentScreenIs(AccountsSettingsScreens.EditAccount) ||
                this.currentScreenIs(AccountsSettingsScreens.EditAccountCurrency) -> R.string.accounts
        this.currentScreenIs(CategoriesSettingsScreens.EditCategories) ||
                this.currentScreenIs(CategoriesSettingsScreens.EditSubcategories) ||
                this.currentScreenIs(CategoriesSettingsScreens.EditCategory) -> R.string.categories
        else -> R.string.settings
    }
}


fun KClass<out Any>.simpleName(): String? {
    return this.simpleName?.substringAfterLast('$')?.substringBefore("(")
}
