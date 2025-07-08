package com.ataglance.walletglance.settings.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.navigation.AuthScreens
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens

sealed class SettingsCategory(
    val screen: Any,
    @StringRes val stringRes: Int,
    @DrawableRes val iconRes: Int
) {

    data class Accounts(val appTheme: AppTheme) : SettingsCategory(
        screen = SettingsScreens.Accounts,
        stringRes = R.string.accounts,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.accounts_light_default_icon
            AppTheme.DarkDefault -> R.drawable.accounts_dark_default_icon
        }
    )

    data class Budgets(val appTheme: AppTheme) : SettingsCategory(
        screen = SettingsScreens.Budgets,
        stringRes = R.string.budgets,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.budgets_light_default_icon
            AppTheme.DarkDefault -> R.drawable.budgets_dark_default_icon
        }
    )

    data class Categories(val appTheme: AppTheme) : SettingsCategory(
        screen = SettingsScreens.Categories,
        stringRes = R.string.categories,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.categories_light_default_icon
            AppTheme.DarkDefault -> R.drawable.categories_dark_default_icon
        }
    )

    data class CategoryCollections(val appTheme: AppTheme) : SettingsCategory(
        screen = SettingsScreens.CategoryCollections,
        stringRes = R.string.category_collections,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.category_collections_light_default_icon
            AppTheme.DarkDefault -> R.drawable.category_collections_dark_default_icon
        }
    )

    data class Appearance(val appTheme: AppTheme) : SettingsCategory(
        screen = SettingsScreens.Personalisation,
        stringRes = R.string.appearance,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.appearance_light_default_icon
            AppTheme.DarkDefault -> R.drawable.appearance_dark_default_icon
        }
    )

    data class ColorTheme(val appTheme: AppTheme) : SettingsCategory(
        screen = SettingsScreens.Personalisation,
        stringRes = R.string.color_theme,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.color_theme_light_default_icon
            AppTheme.DarkDefault -> R.drawable.color_theme_dark_default_icon
        }
    )

    data class Widgets(val appTheme: AppTheme) : SettingsCategory(
        screen = SettingsScreens.Personalisation,
        stringRes = R.string.widgets,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.widgets_light_default_icon
            AppTheme.DarkDefault -> R.drawable.widgets_dark_default_icon
        }
    )

    data class NavigationButtons(val appTheme: AppTheme) : SettingsCategory(
        screen = SettingsScreens.Personalisation,
        stringRes = R.string.navigation_buttons,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.navigation_buttons_light_default_icon
            AppTheme.DarkDefault -> R.drawable.navigation_buttons_dark_default_icon
        }
    )

    data class Notifications(val appTheme: AppTheme) : SettingsCategory(
        screen = SettingsScreens.Notifications,
        stringRes = R.string.notifications,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.notifications_light_default_icon
            AppTheme.DarkDefault -> R.drawable.notifications_dark_default_icon
        }
    )

    data class Language(val appTheme: AppTheme) : SettingsCategory(
        screen = SettingsScreens.Language,
        stringRes = R.string.language,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.language_light_default_icon
            AppTheme.DarkDefault -> R.drawable.language_dark_default_icon
        }
    )

    data class SignIn(val appTheme: AppTheme) : SettingsCategory(
        screen = AuthScreens.SignIn(),
        stringRes = R.string.sign_in,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.sign_in_light_default
            AppTheme.DarkDefault -> R.drawable.sign_in_dark_default
        }
    )

    data class Profile(val appTheme: AppTheme) : SettingsCategory(
        screen = AuthScreens.Profile,
        stringRes = R.string.profile,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.profile_light_default
            AppTheme.DarkDefault -> R.drawable.profile_dark_default
        }
    )

    data class DeleteAccount(val appTheme: AppTheme) : SettingsCategory(
        screen = AuthScreens.DeleteAccount,
        stringRes = R.string.delete_account,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.delete_account_light_default
            AppTheme.DarkDefault -> R.drawable.delete_account_dark_default
        }
    )

    data class SignOut(val appTheme: AppTheme) : SettingsCategory(
        screen = AuthScreens.Profile,
        stringRes = R.string.sign_out,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.sign_out_light_default
            AppTheme.DarkDefault -> R.drawable.sign_out_dark_default
        }
    )

    data class UpdateEmail(val appTheme: AppTheme) : SettingsCategory(
        screen = AuthScreens.UpdateEmail,
        stringRes = R.string.update_email,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.email_light_default
            AppTheme.DarkDefault -> R.drawable.email_dark_default
        }
    )

    data class UpdatePassword(val appTheme: AppTheme) : SettingsCategory(
        screen = AuthScreens.UpdatePassword,
        stringRes = R.string.update_password,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.password_light_default
            AppTheme.DarkDefault -> R.drawable.password_dark_default
        }
    )

    data class ManageSubscriptions(val appTheme: AppTheme) : SettingsCategory(
        screen = AuthScreens.ManageSubscriptions,
        stringRes = R.string.manage_subscriptions,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.manage_subscriptions_light_default
            AppTheme.DarkDefault -> R.drawable.manage_subscriptions_dark_default
        }
    )

    data class ResetData(val appTheme: AppTheme) : SettingsCategory(
        screen = SettingsScreens.ResetData,
        stringRes = R.string.reset_data,
        iconRes = when (appTheme) {
            AppTheme.LightDefault -> R.drawable.reset_light_default_icon
            AppTheme.DarkDefault -> R.drawable.reset_dark_default_icon
        }
    )


    companion object {

        fun asList(appTheme: AppTheme, isSignedIn: Boolean): List<SettingsCategory> {
            return listOf(
                if (isSignedIn) Profile(appTheme) else SignIn(appTheme),
                Accounts(appTheme),
                Budgets(appTheme),
                Categories(appTheme),
                CategoryCollections(appTheme),
                Appearance(appTheme),
                Notifications(appTheme),
                Language(appTheme),
                ResetData(appTheme),
            )
        }

    }

}