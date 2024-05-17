package com.ataglance.walletglance.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ataglance.walletglance.R
import com.ataglance.walletglance.ui.theme.theme.AppTheme

enum class MakeRecordScreenArgs {
    Status, RecordNum
}
enum class MakeRecordStatus {
    Create, Edit
}

enum class AppScreenRoutes {
    Home, Records, CategoriesStatistics, MakeRecord, MakeTransfer, Settings, FinishSetup
}
sealed class AppScreen(val route: String) {
    data object Home : AppScreen(AppScreenRoutes.Home.name)
    data object Records : AppScreen(AppScreenRoutes.Records.name)
    data object CategoriesStatistics : AppScreen(AppScreenRoutes.CategoriesStatistics.name)
    data object MakeRecord : AppScreen(AppScreenRoutes.MakeRecord.name)
    data object MakeTransfer : AppScreen(AppScreenRoutes.MakeTransfer.name)
    data object Settings : AppScreen(AppScreenRoutes.Settings.name)
    data object FinishSetup : AppScreen(AppScreenRoutes.FinishSetup.name)
}

enum class CategoryStatisticsScreenArgs {
    ParentCategoryId
}
enum class EditAccountScreenArgs {
    OrderNum
}
enum class CurrencyPickerScreenArgs {
    Currency
}
enum class EditSubcategoryListScreenArgs {
    ParentCategoryOrderNum
}

enum class SettingsRoutes {
    Start, SettingsHome, Language, Appearance, Accounts, Categories, Import, Data
}
sealed class SettingsScreen(val route: String) {
    data object Start : SettingsScreen(SettingsRoutes.Start.name)
    data object SettingsHome : SettingsScreen(SettingsRoutes.SettingsHome.name)
    data object Language : SettingsScreen(SettingsRoutes.Language.name)
    data object Appearance : SettingsScreen(SettingsRoutes.Appearance.name)
    data object Accounts : SettingsScreen(SettingsRoutes.Accounts.name)
    data object Categories : SettingsScreen(SettingsRoutes.Categories.name)
    data object Import : SettingsScreen(SettingsRoutes.Import.name)
    data object Data : SettingsScreen(SettingsRoutes.Data.name)
}

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

enum class AccountsSetupRoutes {
    AccountsSetup, EditAccount, CurrencyPicker
}

sealed class AccountsSetupScreen(val route: String) {
    data object AccountsSetup : AccountsSetupScreen(AccountsSetupRoutes.AccountsSetup.name)
    data object EditAccount : AccountsSetupScreen(AccountsSetupRoutes.EditAccount.name)
    data object CurrencyPicker : AccountsSetupScreen(AccountsSetupRoutes.CurrencyPicker.name)
}

enum class CategoriesSetupRoutes {
    CategoriesSetup, SubcategoriesSetup, EditCategory
}

sealed class CategoriesSetupScreen(val route: String) {
    data object CategoriesSetup : CategoriesSetupScreen(CategoriesSetupRoutes.CategoriesSetup.name)
    data object SubcategoriesSetup : CategoriesSetupScreen(CategoriesSetupRoutes.SubcategoriesSetup.name)
    data object EditCategory : CategoriesSetupScreen(CategoriesSetupRoutes.EditCategory.name)
}