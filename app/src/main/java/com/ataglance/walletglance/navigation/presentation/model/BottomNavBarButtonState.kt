package com.ataglance.walletglance.navigation.presentation.model

import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.DrawableResByTheme
import com.ataglance.walletglance.core.domain.navigation.MainScreens

sealed class BottomNavBarButtonState(
    val screen: MainScreens,
    @StringRes val screenNameRes: Int,
    val iconRes: DrawableResByTheme,
    open val isActive: Boolean
) {

    data class Home(override val isActive: Boolean = false) : BottomNavBarButtonState(
        screen = MainScreens.Home,
        screenNameRes = R.string.home,
        iconRes = DrawableResByTheme(
            lightDefault = if (isActive) R.drawable.home_light_active else R.drawable.home_light_inactive,
            darkDefault = if (isActive) R.drawable.home_dark_active else R.drawable.home_dark_inactive
        ),
        isActive = isActive
    )

    data class Records(override val isActive: Boolean = false) : BottomNavBarButtonState(
        screen = MainScreens.Transactions,
        screenNameRes = R.string.records,
        iconRes = DrawableResByTheme(
            lightDefault = if (isActive) R.drawable.records_light_active else R.drawable.records_light_inactive,
            darkDefault = if (isActive) R.drawable.records_dark_active else R.drawable.records_dark_inactive
        ),
        isActive = isActive
    )

    data class CategoryStatistics(override val isActive: Boolean = false) : BottomNavBarButtonState(
        screen = MainScreens.CategoryStatistics(),
        screenNameRes = R.string.statistics,
        iconRes = DrawableResByTheme(
            lightDefault = if (isActive) R.drawable.statistics_light_active else R.drawable.statistics_light_inactive,
            darkDefault = if (isActive) R.drawable.statistics_dark_active else R.drawable.statistics_dark_inactive
        ),
        isActive = isActive
    )

    data class Budgets(override val isActive: Boolean = false) : BottomNavBarButtonState(
        screen = MainScreens.Budgets,
        screenNameRes = R.string.budgets,
        iconRes = DrawableResByTheme(
            lightDefault = if (isActive) R.drawable.budgets_light_active else R.drawable.budgets_light_inactive,
            darkDefault = if (isActive) R.drawable.budgets_dark_active else R.drawable.budgets_dark_inactive
        ),
        isActive = isActive
    )

    data class Settings(override val isActive: Boolean = false) : BottomNavBarButtonState(
        screen = MainScreens.Settings,
        screenNameRes = R.string.settings,
        iconRes = DrawableResByTheme(
            lightDefault = if (isActive) R.drawable.settings_light_active else R.drawable.settings_light_inactive,
            darkDefault = if (isActive) R.drawable.settings_dark_active else R.drawable.settings_dark_inactive
        ),
        isActive = isActive
    )

    data class Other(override val isActive: Boolean = false) : BottomNavBarButtonState(
        screen = MainScreens.Home,
        screenNameRes = R.string.other,
        iconRes = DrawableResByTheme(
            lightDefault = if (isActive) R.drawable.show_other_light_active else R.drawable.show_other_light_inactive,
            darkDefault = if (isActive) R.drawable.show_other_dark_active else R.drawable.show_other_dark_inactive
        ),
        isActive = isActive
    )


    fun updateActive(isActive: Boolean): BottomNavBarButtonState {
        return when (this) {
            is Home -> this.copy(isActive = isActive)
            is Records -> this.copy(isActive = isActive)
            is CategoryStatistics -> this.copy(isActive = isActive)
            is Budgets -> this.copy(isActive = isActive)
            is Settings -> this.copy(isActive = isActive)
            is Other -> this.copy(isActive = isActive)
        }
    }

    fun toggleActive(): BottomNavBarButtonState {
        return when (this) {
            is Home -> this.copy(isActive = !isActive)
            is Records -> this.copy(isActive = !isActive)
            is CategoryStatistics -> this.copy(isActive = !isActive)
            is Budgets -> this.copy(isActive = !isActive)
            is Settings -> this.copy(isActive = !isActive)
            is Other -> this.copy(isActive = !isActive)
        }
    }


    companion object {

        fun asDefaultList(): List<BottomNavBarButtonState> = listOf(
            Home(isActive = false),
            Records(isActive = false),
            CategoryStatistics(isActive = false),
            Budgets(isActive = false),
            Settings(isActive = false)
        )

    }

}