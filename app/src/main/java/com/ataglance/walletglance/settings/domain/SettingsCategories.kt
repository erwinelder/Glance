package com.ataglance.walletglance.settings.domain

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.presentation.navigation.AuthScreens
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.settings.navigation.SettingsScreens

data class SettingsCategory(
    val screen: Any,
    @StringRes val stringRes: Int,
    @DrawableRes val iconRes: Int
)

class SettingsCategories(appTheme: AppTheme) {

    val accounts = SettingsCategory(
        screen = SettingsScreens.Accounts,
        stringRes = R.string.accounts,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.accounts_light_default_icon
            AppTheme.DarkDefault -> R.drawable.accounts_dark_default_icon
        }
    )

    val budgets = SettingsCategory(
        screen = SettingsScreens.Budgets,
        stringRes = R.string.budgets,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.budgets_light_default_icon
            AppTheme.DarkDefault -> R.drawable.budgets_dark_default_icon
        }
    )

    val categories = SettingsCategory(
        screen = SettingsScreens.Categories,
        stringRes = R.string.categories,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.categories_light_default_icon
            AppTheme.DarkDefault -> R.drawable.categories_dark_default_icon
        }
    )

    val categoryCollections = SettingsCategory(
        screen = SettingsScreens.CategoryCollections,
        stringRes = R.string.category_collections,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.category_collections_light_default_icon
            AppTheme.DarkDefault -> R.drawable.category_collections_dark_default_icon
        }
    )

    val appearance = SettingsCategory(
        screen = SettingsScreens.Appearance,
        stringRes = R.string.appearance,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.appearance_light_default_icon
            AppTheme.DarkDefault -> R.drawable.appearance_dark_default_icon
        }
    )

    val colorTheme = SettingsCategory(
        screen = SettingsScreens.Appearance,
        stringRes = R.string.color_theme,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.color_theme_light_default_icon
            AppTheme.DarkDefault -> R.drawable.color_theme_dark_default_icon
        }
    )

    val widgets = SettingsCategory(
        screen = SettingsScreens.Appearance,
        stringRes = R.string.widgets,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.widgets_light_default_icon
            AppTheme.DarkDefault -> R.drawable.widgets_dark_default_icon
        }
    )

    val navigationButtons = SettingsCategory(
        screen = SettingsScreens.Appearance,
        stringRes = R.string.navigation_buttons,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.navigation_buttons_light_default_icon
            AppTheme.DarkDefault -> R.drawable.navigation_buttons_dark_default_icon
        }
    )

    val language = SettingsCategory(
        screen = SettingsScreens.Language,
        stringRes = R.string.language,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.language_light_default_icon
            AppTheme.DarkDefault -> R.drawable.language_dark_default_icon
        }
    )

    val signIn = SettingsCategory(
        screen = AuthScreens.SignIn,
        stringRes = R.string.sign_in,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.sign_in_light_default
            AppTheme.DarkDefault -> R.drawable.sign_in_dark_default
        }
    )

    val profile = SettingsCategory(
        screen = AuthScreens.Profile,
        stringRes = R.string.profile,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.profile_light_default
            AppTheme.DarkDefault -> R.drawable.profile_dark_default
        }
    )

    val deleteAccount = SettingsCategory(
        screen = AuthScreens.DeleteAccount,
        stringRes = R.string.delete_account,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.delete_account_light_default
            AppTheme.DarkDefault -> R.drawable.delete_account_dark_default
        }
    )

    val signOut = SettingsCategory(
        screen = AuthScreens.Profile,
        stringRes = R.string.sign_out,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.sign_out_light_default
            AppTheme.DarkDefault -> R.drawable.sign_out_dark_default
        }
    )

    val updateEmail = SettingsCategory(
        screen = AuthScreens.UpdateEmail,
        stringRes = R.string.update_email,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.email_light_default
            AppTheme.DarkDefault -> R.drawable.email_dark_default
        }
    )

    val updatePassword = SettingsCategory(
        screen = AuthScreens.UpdatePassword,
        stringRes = R.string.update_password,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.password_light_default
            AppTheme.DarkDefault -> R.drawable.password_dark_default
        }
    )

    val manageSubscriptions = SettingsCategory(
        screen = AuthScreens.ManageSubscriptions,
        stringRes = R.string.manage_subscriptions,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.manage_subscriptions_light_default
            AppTheme.DarkDefault -> R.drawable.manage_subscriptions_dark_default
        }
    )

    val resetData = SettingsCategory(
        screen = SettingsScreens.ResetData,
        stringRes = R.string.reset_data,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.reset_light_default_icon
            AppTheme.DarkDefault -> R.drawable.reset_dark_default_icon
        }
    )


    fun getCategories(isSignedIn: Boolean): List<SettingsCategory> {
        return listOf(
            if (isSignedIn) profile else signIn,
            accounts,
            budgets,
            categories,
            categoryCollections,
            appearance,
            language,
            resetData
        )
    }

}