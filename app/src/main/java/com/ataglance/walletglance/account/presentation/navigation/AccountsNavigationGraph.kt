package com.ataglance.walletglance.account.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ataglance.walletglance.account.domain.navigation.AccountsSettingsScreens
import com.ataglance.walletglance.account.presentation.screen.CurrencyPickerScreenWrapper
import com.ataglance.walletglance.account.presentation.screen.EditAccountScreenWrapper
import com.ataglance.walletglance.account.presentation.screen.EditAccountsScreenWrapper
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens

fun NavGraphBuilder.accountsGraph(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appConfiguration: AppConfiguration
) {
    navigation<SettingsScreens.Accounts>(
        startDestination = AccountsSettingsScreens.EditAccounts
    ) {
        composable<AccountsSettingsScreens.EditAccounts> { backStack ->
            EditAccountsScreenWrapper(
                screenPadding = screenPadding,
                backStack = backStack,
                navController = navController,
                navViewModel = navViewModel,
                appConfiguration = appConfiguration
            )
        }
        composable<AccountsSettingsScreens.EditAccount> { backStack ->
            EditAccountScreenWrapper(
                screenPadding = screenPadding,
                backStack = backStack,
                navController = navController,
                navViewModel = navViewModel
            )
        }
        composable<AccountsSettingsScreens.EditAccountCurrency> { backStack ->
            CurrencyPickerScreenWrapper(
                screenPadding = screenPadding,
                backStack = backStack,
                navController = navController
            )
        }
    }
}