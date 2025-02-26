package com.ataglance.walletglance.navigation.domain.model

import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.DrawableResByTheme
import com.ataglance.walletglance.core.domain.navigation.MainScreens

sealed class BottomBarNavigationButton(
    val screen: MainScreens,
    @StringRes val screenNameRes: Int,
    val inactiveIconRes: DrawableResByTheme,
    val activeIconRes: DrawableResByTheme,
) {

    data object Home : BottomBarNavigationButton(
        screen = MainScreens.Home,
        screenNameRes = R.string.home,
        inactiveIconRes = DrawableResByTheme(
            lightDefault = R.drawable.home_light_inactive,
            darkDefault = R.drawable.home_dark_inactive
        ),
        activeIconRes = DrawableResByTheme(
            lightDefault = R.drawable.home_light_active,
            darkDefault = R.drawable.home_dark_active
        )
    )

    data object Records : BottomBarNavigationButton(
        screen = MainScreens.Records,
        screenNameRes = R.string.records,
        inactiveIconRes = DrawableResByTheme(
            lightDefault = R.drawable.records_light_inactive,
            darkDefault = R.drawable.records_dark_inactive
        ),
        activeIconRes = DrawableResByTheme(
            lightDefault = R.drawable.records_light_active,
            darkDefault = R.drawable.records_dark_active
        )
    )

    data object CategoryStatistics : BottomBarNavigationButton(
        screen = MainScreens.CategoryStatistics(),
        screenNameRes = R.string.statistics,
        inactiveIconRes = DrawableResByTheme(
            lightDefault = R.drawable.statistics_light_inactive,
            darkDefault = R.drawable.statistics_dark_inactive
        ),
        activeIconRes = DrawableResByTheme(
            lightDefault = R.drawable.statistics_light_active,
            darkDefault = R.drawable.statistics_dark_active
        )
    )

    data object Budgets : BottomBarNavigationButton(
        screen = MainScreens.Budgets,
        screenNameRes = R.string.budgets,
        inactiveIconRes = DrawableResByTheme(
            lightDefault = R.drawable.budgets_light_inactive,
            darkDefault = R.drawable.budgets_dark_inactive
        ),
        activeIconRes = DrawableResByTheme(
            lightDefault = R.drawable.budgets_light_active,
            darkDefault = R.drawable.budgets_dark_active
        )
    )

    data object Settings : BottomBarNavigationButton(
        screen = MainScreens.Settings,
        screenNameRes = R.string.settings,
        inactiveIconRes = DrawableResByTheme(
            lightDefault = R.drawable.settings_light_inactive,
            darkDefault = R.drawable.settings_dark_inactive
        ),
        activeIconRes = DrawableResByTheme(
            lightDefault = R.drawable.settings_light_active,
            darkDefault = R.drawable.settings_dark_active
        )
    )


    companion object {

        fun asDefaultList(): List<BottomBarNavigationButton> {
            return listOf(
                Home,
                Records,
                CategoryStatistics,
                Budgets,
                Settings
            )
        }

    }

}
