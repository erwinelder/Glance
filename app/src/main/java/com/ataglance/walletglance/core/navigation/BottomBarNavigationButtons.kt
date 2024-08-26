package com.ataglance.walletglance.core.navigation

import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.DrawableResByTheme

sealed class BottomBarNavigationButtons(
    val screen: MainScreens,
    val inactiveIconRes: DrawableResByTheme,
    val activeIconRes: DrawableResByTheme,
) {

    data object Home : BottomBarNavigationButtons(
        screen = MainScreens.Home,
        inactiveIconRes = DrawableResByTheme(
            lightDefault = R.drawable.home_light_inactive,
            darkDefault = R.drawable.home_dark_inactive
        ),
        activeIconRes = DrawableResByTheme(
            lightDefault = R.drawable.home_light_active,
            darkDefault = R.drawable.home_dark_active
        )
    )

    data object Records : BottomBarNavigationButtons(
        screen = MainScreens.Records,
        inactiveIconRes = DrawableResByTheme(
            lightDefault = R.drawable.records_light_inactive,
            darkDefault = R.drawable.records_dark_inactive
        ),
        activeIconRes = DrawableResByTheme(
            lightDefault = R.drawable.records_light_active,
            darkDefault = R.drawable.records_dark_active
        )
    )

    data object CategoryStatistics : BottomBarNavigationButtons(
        screen = MainScreens.CategoryStatistics(0),
        inactiveIconRes = DrawableResByTheme(
            lightDefault = R.drawable.statistics_light_inactive,
            darkDefault = R.drawable.statistics_dark_inactive
        ),
        activeIconRes = DrawableResByTheme(
            lightDefault = R.drawable.statistics_light_active,
            darkDefault = R.drawable.statistics_dark_active
        )
    )

    data object Budgets : BottomBarNavigationButtons(
        screen = MainScreens.Budgets,
        inactiveIconRes = DrawableResByTheme(
            lightDefault = R.drawable.budgets_light_inactive,
            darkDefault = R.drawable.budgets_dark_inactive
        ),
        activeIconRes = DrawableResByTheme(
            lightDefault = R.drawable.budgets_light_active,
            darkDefault = R.drawable.budgets_dark_active
        )
    )

    data object Settings : BottomBarNavigationButtons(
        screen = MainScreens.Settings,
        inactiveIconRes = DrawableResByTheme(
            lightDefault = R.drawable.settings_light_inactive,
            darkDefault = R.drawable.settings_dark_inactive
        ),
        activeIconRes = DrawableResByTheme(
            lightDefault = R.drawable.settings_light_active,
            darkDefault = R.drawable.settings_dark_active
        )
    )

}
