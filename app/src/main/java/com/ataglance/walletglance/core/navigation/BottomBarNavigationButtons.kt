package com.ataglance.walletglance.core.navigation

import androidx.annotation.DrawableRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme

sealed class BottomBarNavigationButtons(
    val screen: MainScreens,
    val inactiveIconRes: BottomBarIcon,
    val activeIconRes: BottomBarIcon,
) {

    data object Home : BottomBarNavigationButtons(
        screen = MainScreens.Home,
        inactiveIconRes = BottomBarIcon(
            lightDefault = R.drawable.home_light_inactive,
            darkDefault = R.drawable.home_dark_inactive
        ),
        activeIconRes = BottomBarIcon(
            lightDefault = R.drawable.home_light_active,
            darkDefault = R.drawable.home_dark_active
        )
    )

    data object Records : BottomBarNavigationButtons(
        screen = MainScreens.Records,
        inactiveIconRes = BottomBarIcon(
            lightDefault = R.drawable.records_light_inactive,
            darkDefault = R.drawable.records_dark_inactive
        ),
        activeIconRes = BottomBarIcon(
            lightDefault = R.drawable.records_light_active,
            darkDefault = R.drawable.records_dark_active
        )
    )

    data object CategoryStatistics : BottomBarNavigationButtons(
        screen = MainScreens.CategoryStatistics(0),
        inactiveIconRes = BottomBarIcon(
            lightDefault = R.drawable.statistics_light_inactive,
            darkDefault = R.drawable.statistics_dark_inactive
        ),
        activeIconRes = BottomBarIcon(
            lightDefault = R.drawable.statistics_light_active,
            darkDefault = R.drawable.statistics_dark_active
        )
    )

    data object Settings : BottomBarNavigationButtons(
        screen = MainScreens.Settings,
        inactiveIconRes = BottomBarIcon(
            lightDefault = R.drawable.settings_light_inactive,
            darkDefault = R.drawable.settings_dark_inactive
        ),
        activeIconRes = BottomBarIcon(
            lightDefault = R.drawable.settings_light_active,
            darkDefault = R.drawable.settings_dark_active
        )
    )

}

data class BottomBarIcon(
    @DrawableRes val lightDefault: Int,
    @DrawableRes val darkDefault: Int
) {

    fun getByTheme(theme: AppTheme?): Int {
        return when (theme) {
            AppTheme.DarkDefault -> darkDefault
            else -> lightDefault
        }
    }

}