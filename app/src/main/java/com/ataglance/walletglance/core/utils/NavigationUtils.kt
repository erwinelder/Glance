package com.ataglance.walletglance.core.utils

import androidx.annotation.StringRes
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.navigation.AccountsSettingsScreens
import com.ataglance.walletglance.category.navigation.CategoriesSettingsScreens
import com.ataglance.walletglance.navigation.domain.model.MainScreens
import com.ataglance.walletglance.settings.navigation.SettingsScreens
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


fun Any.isScreen(screen: Any): Boolean {
    return this::class.simpleName() == screen.toString()
}


fun NavDestination?.currentScreenIs(screen: Any): Boolean {
    return this?.fromRoute() == screen::class.simpleName()
}


fun NavBackStackEntry?.currentScreenIs(screen: Any): Boolean {
    return this?.fromRoute() == screen::class.simpleName() ||
            (this?.fromRoute() == SettingsScreens.SettingsHome::class.simpleName() &&
                    screen::class.simpleName() == MainScreens.Settings::class.simpleName())
}


fun NavBackStackEntry?.currentScreenIsOneOf(vararg screens: Any): Boolean {
    screens.forEach { screen ->
        if (this.currentScreenIs(screen)) {
            return true
        }
    }
    return false
}


fun NavBackStackEntry?.currentScreenIsNoneOf(vararg screens: Any): Boolean {
    screens.forEach { screen ->
        if (this.currentScreenIs(screen)) {
            return false
        }
    }
    return true
}


fun NavBackStackEntry?.anyScreenInHierarchyIs(screen: Any): Boolean {
    return this?.destination?.hierarchy?.any {
        it.currentScreenIs(screen)
    } == true
}


@StringRes fun NavBackStackEntry?.getSetupProgressTopBarTitleRes(): Int {
    return when {
        this.currentScreenIs(SettingsScreens.Language) -> R.string.language
        this.currentScreenIs(SettingsScreens.Appearance) -> R.string.appearance
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
    return this.simpleName?.substringAfterLast('$')
}
