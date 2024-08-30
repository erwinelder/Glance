package com.ataglance.walletglance.account.navigation

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
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.presentation.screen.CurrencyPickerScreen
import com.ataglance.walletglance.account.presentation.screen.EditAccountScreen
import com.ataglance.walletglance.account.presentation.screen.EditAccountsScreen
import com.ataglance.walletglance.account.presentation.viewmodel.CurrencyPickerViewModel
import com.ataglance.walletglance.account.presentation.viewmodel.CurrencyPickerViewModelFactory
import com.ataglance.walletglance.account.presentation.viewmodel.EditAccountViewModel
import com.ataglance.walletglance.account.presentation.viewmodel.EditAccountsViewModel
import com.ataglance.walletglance.account.presentation.viewmodel.EditAccountsViewModelFactory
import com.ataglance.walletglance.core.domain.app.AppUiSettings
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.settings.navigation.SettingsScreens
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

            val accountsList by accountsViewModel.accountsUiState.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            EditAccountsScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = appUiSettings.isSetUp,
                appTheme = appUiSettings.appTheme,
                accountsList = accountsList,
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