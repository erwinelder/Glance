package com.ataglance.walletglance.model

import androidx.annotation.DrawableRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.ui.theme.theme.AppTheme

sealed class BottomBarButtons(
    val route: String,
    val relatedScreen: AppScreen,
    @DrawableRes val iconRes: Int
) {
    data class HomeInactive(val theme: AppTheme?) : BottomBarButtons(
        route = AppScreen.Home.route,
        relatedScreen = AppScreen.Home,
        iconRes = when (theme) {
            AppTheme.DarkDefault -> R.drawable.home_dark_inactive
            else -> R.drawable.home_light_inactive
        }
    )
    data class HomeActive(val theme: AppTheme?) : BottomBarButtons(
        route = AppScreen.Home.route,
        relatedScreen = AppScreen.Home,
        iconRes = when (theme) {
            AppTheme.DarkDefault -> R.drawable.home_dark_active
            else -> R.drawable.home_light_active
        }
    )
    data class RecordsInactive(val theme: AppTheme?) : BottomBarButtons(
        route = AppScreen.Records.route,
        relatedScreen = AppScreen.Records,
        iconRes = when (theme) {
            AppTheme.DarkDefault -> R.drawable.records_dark_inactive
            else -> R.drawable.records_light_inactive
        }
    )
    data class RecordsActive(val theme: AppTheme?) : BottomBarButtons(
        route = AppScreen.Records.route,
        relatedScreen = AppScreen.Records,
        iconRes = when (theme) {
            AppTheme.DarkDefault -> R.drawable.records_dark_active
            else -> R.drawable.records_light_active
        }
    )
    data class CategoriesStatisticsInactive(val theme: AppTheme?) : BottomBarButtons(
        route = "${AppScreen.CategoriesStatistics.route}/0",
        relatedScreen = AppScreen.CategoriesStatistics,
        iconRes = when (theme) {
            AppTheme.DarkDefault -> R.drawable.statistics_dark_inactive
            else -> R.drawable.statistics_light_inactive
        }
    )
    data class CategoriesStatisticsActive(val theme: AppTheme?) : BottomBarButtons(
        route = "${AppScreen.CategoriesStatistics.route}/0",
        relatedScreen = AppScreen.CategoriesStatistics,
        iconRes = when (theme) {
            AppTheme.DarkDefault -> R.drawable.statistics_dark_active
            else -> R.drawable.statistics_light_active
        }
    )
    data class SettingsInactive(val theme: AppTheme?) : BottomBarButtons(
        route = AppScreen.Settings.route,
        relatedScreen = AppScreen.Settings,
        iconRes = when (theme) {
            AppTheme.DarkDefault -> R.drawable.settings_dark_inactive
            else -> R.drawable.settings_light_inactive
        }
    )
    data class SettingsActive(val theme: AppTheme?) : BottomBarButtons(
        route = AppScreen.Settings.route,
        relatedScreen = AppScreen.Settings,
        iconRes = when (theme) {
            AppTheme.DarkDefault -> R.drawable.settings_dark_active
            else -> R.drawable.settings_light_active
        }
    )
}