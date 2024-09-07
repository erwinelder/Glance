package com.ataglance.walletglance.settings.domain

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.settings.navigation.SettingsScreens

data class SettingsCategory(
    val screen: SettingsScreens,
    @StringRes val stringRes: Int,
    @DrawableRes val iconRes: Int
)

class SettingsCategories(appTheme: AppTheme) {

    val accounts = SettingsCategory(
        SettingsScreens.Accounts,
        R.string.accounts,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.accounts_light_default_icon
            AppTheme.DarkDefault -> R.drawable.accounts_dark_default_icon
        }
    )

    val budgets = SettingsCategory(
        SettingsScreens.Budgets,
        R.string.budgets,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.budgets_light_default_icon
            AppTheme.DarkDefault -> R.drawable.budgets_dark_default_icon
        }
    )

    val categories = SettingsCategory(
        SettingsScreens.Categories,
        R.string.categories,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.categories_light_default_icon
            AppTheme.DarkDefault -> R.drawable.categories_dark_default_icon
        }
    )

    val categoryCollections = SettingsCategory(
        SettingsScreens.CategoryCollections,
        R.string.category_collections,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.category_collections_light_default_icon
            AppTheme.DarkDefault -> R.drawable.category_collections_dark_default_icon
        }
    )

    val appearance = SettingsCategory(
        SettingsScreens.Appearance,
        R.string.appearance,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.appearance_light_default_icon
            AppTheme.DarkDefault -> R.drawable.appearance_dark_default_icon
        }
    )

    val colorTheme = SettingsCategory(
        SettingsScreens.Appearance,
        R.string.color_theme,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.color_theme_light_default_icon
            AppTheme.DarkDefault -> R.drawable.color_theme_dark_default_icon
        }
    )

    val widgets = SettingsCategory(
        SettingsScreens.Appearance,
        R.string.widgets,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.widgets_light_default_icon
            AppTheme.DarkDefault -> R.drawable.widgets_dark_default_icon
        }
    )

    val navigationButtons = SettingsCategory(
        SettingsScreens.Appearance,
        R.string.navigation_buttons,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.navigation_buttons_light_default_icon
            AppTheme.DarkDefault -> R.drawable.navigation_buttons_dark_default_icon
        }
    )

    val language = SettingsCategory(
        SettingsScreens.Language,
        R.string.language,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.language_light_default_icon
            AppTheme.DarkDefault -> R.drawable.language_dark_default_icon
        }
    )

    val resetData = SettingsCategory(
        SettingsScreens.ResetData,
        R.string.reset_data,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.reset_light_default_icon
            AppTheme.DarkDefault -> R.drawable.reset_dark_default_icon
        }
    )

}