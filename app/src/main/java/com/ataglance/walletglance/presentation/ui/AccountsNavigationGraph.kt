package com.ataglance.walletglance.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.app.AppUiSettings
import com.ataglance.walletglance.presentation.ui.navigation.screens.AccountsSettingsScreens
import com.ataglance.walletglance.presentation.ui.navigation.screens.SettingsScreens
import com.ataglance.walletglance.presentation.ui.screens.settings.accounts.CurrencyPickerScreen
import com.ataglance.walletglance.presentation.ui.screens.settings.accounts.EditAccountScreen
import com.ataglance.walletglance.presentation.ui.screens.settings.accounts.EditAccountsScreen
import com.ataglance.walletglance.presentation.viewmodels.AppViewModel
import com.ataglance.walletglance.presentation.viewmodels.accounts.CurrencyPickerViewModel
import com.ataglance.walletglance.presentation.viewmodels.accounts.CurrencyPickerViewModelFactory
import com.ataglance.walletglance.presentation.viewmodels.accounts.EditAccountViewModel
import com.ataglance.walletglance.presentation.viewmodels.accounts.EditAccountsViewModel
import com.ataglance.walletglance.presentation.viewmodels.accounts.EditAccountsViewModelFactory
import com.ataglance.walletglance.presentation.viewmodels.sharedViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.accountsGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    appViewModel: AppViewModel,
    appUiSettings: AppUiSettings,
    accountList: List<Account>
) {
    navigation<SettingsScreens.Accounts>(startDestination = AccountsSettingsScreens.EditAccounts) {
        composable<AccountsSettingsScreens.EditAccounts> { backStack ->

            val accountsViewModel = backStack.sharedViewModel<EditAccountsViewModel>(
                navController = navController,
                factory = EditAccountsViewModelFactory(accountList)
            )
            val editAccountViewModel = backStack.sharedViewModel<EditAccountViewModel>(
                navController = navController
            )

            val accountsUiState by accountsViewModel.accountsUiState.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            EditAccountsScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = appUiSettings.isSetUp,
                appTheme = appUiSettings.appTheme,
                accountsList = accountsUiState,
                onNavigateToEditAccountScreen = { account ->
                    editAccountViewModel.applyAccountData(
                        account = account ?: accountsViewModel.getNewAccount()
                    )
                    navController.navigate(AccountsSettingsScreens.EditAccount)
                },
                onSwapAccounts = accountsViewModel::swapAccounts,
                onSaveButton = {
                    coroutineScope.launch {
                        appViewModel.saveAccountsToDb(accountsViewModel.getAccountEntities())
                        if (appUiSettings.isSetUp) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(SettingsScreens.Categories)
                        }
                    }
                }
            )
        }
        composable<AccountsSettingsScreens.EditAccount> { backStack ->

            val accountsViewModel = backStack.sharedViewModel<EditAccountsViewModel>(
                navController = navController
            )
            val editAccountViewModel = backStack.sharedViewModel<EditAccountViewModel>(
                navController = navController
            )

            val accountUiState by editAccountViewModel
                .editAccountUiState.collectAsStateWithLifecycle()
            val allowDeleting by accountsViewModel.allowDeleting.collectAsStateWithLifecycle()
            val allowSaving by editAccountViewModel.allowSaving.collectAsStateWithLifecycle()

            EditAccountScreen(
                scaffoldPadding = scaffoldPadding,
                appTheme = appUiSettings.appTheme,
                editAccountUiState = accountUiState,
                allowDeleting = allowDeleting,
                allowSaving = allowSaving,
                onColorChange = editAccountViewModel::changeColor,
                onNameChange = editAccountViewModel::changeName,
                onNavigateToEditAccountCurrencyScreen = {
                    navController.navigate(
                        AccountsSettingsScreens.EditAccountCurrency(accountUiState.currency)
                    )
                },
                onBalanceChange = editAccountViewModel::changeBalance,
                onHideChange = editAccountViewModel::changeHide,
                onHideBalanceChange = editAccountViewModel::changeHideBalance,
                onWithoutBalanceChange = editAccountViewModel::changeWithoutBalance,
                onDeleteButton = { accountId ->
                    navController.popBackStack()
                    accountsViewModel.deleteAccountById(accountId)
                },
                onSaveButton = {
                    accountsViewModel.saveAccount(editAccountViewModel.getAccount())
                    navController.popBackStack()
                }
            )
        }
        composable<AccountsSettingsScreens.EditAccountCurrency> { backStack ->
            val currency = backStack.toRoute<AccountsSettingsScreens.EditAccountCurrency>().currency

            val editAccountViewModel = backStack.sharedViewModel<EditAccountViewModel>(
                navController = navController
            )
            val currencyPickerViewModel = viewModel<CurrencyPickerViewModel>(
                factory = CurrencyPickerViewModelFactory(
                    selectedCurrency = currency
                )
            )

            val uiState by currencyPickerViewModel.uiState.collectAsStateWithLifecycle()
            val currencyList by currencyPickerViewModel.currencyList.collectAsStateWithLifecycle()
            val searchedPrompt by currencyPickerViewModel
                .searchedPrompt.collectAsStateWithLifecycle()

            CurrencyPickerScreen(
                scaffoldPadding = scaffoldPadding,
                uiState = uiState,
                currencyList = currencyList,
                searchedPrompt = searchedPrompt,
                onSearchPromptChange = currencyPickerViewModel::changeSearchPrompt,
                onSelectCurrency = currencyPickerViewModel::selectCurrency,
                onSaveButtonClick = { selectedCurrency ->
                    editAccountViewModel.changeCurrency(selectedCurrency)
                    navController.popBackStack()
                }
            )
        }
    }
}