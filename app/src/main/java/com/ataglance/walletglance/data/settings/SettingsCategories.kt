package com.ataglance.walletglance.data.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.app.AppTheme

data class SettingsCategory(@StringRes val stringRes: Int, @DrawableRes val iconRes: Int)

class SettingsCategories(appTheme: AppTheme?) {

    val accounts = SettingsCategory(
        R.string.accounts,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.accounts_light_default_icon
            AppTheme.DarkDefault -> R.drawable.accounts_dark_default_icon
            else -> R.drawable.accounts_light_default_icon
        }
    )

    val categories = SettingsCategory(
        R.string.categories,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.categories_light_default_icon
            AppTheme.DarkDefault -> R.drawable.categories_dark_default_icon
            else -> R.drawable.categories_light_default_icon
        }
    )

    val categoryCollections = SettingsCategory(
        R.string.category_collections,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.category_collections_light_default_icon_
            AppTheme.DarkDefault -> R.drawable.category_collections_dark_default_icon_
            else -> R.drawable.category_collections_light_default_icon_
        }
    )

    val appearance = SettingsCategory(
        R.string.appearance,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.appearance_light_default_icon
            AppTheme.DarkDefault -> R.drawable.appearance_dark_default_icon
            else -> R.drawable.appearance_light_default_icon
        }
    )

    val language = SettingsCategory(
        R.string.language,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.language_light_default_icon
            AppTheme.DarkDefault -> R.drawable.language_dark_default_icon
            else -> R.drawable.language_light_default_icon
        }
    )

    val resetData = SettingsCategory(
        R.string.reset_data,
        when (appTheme) {
            AppTheme.LightDefault -> R.drawable.reset_light_default_icon
            AppTheme.DarkDefault -> R.drawable.reset_dark_default_icon
            else -> R.drawable.reset_light_default_icon
        }
    )

}