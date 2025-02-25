package com.ataglance.walletglance.account.presentation.navigation

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
import com.ataglance.walletglance.account.domain.navigation.AccountsSettingsScreens
import com.ataglance.walletglance.account.presentation.screen.CurrencyPickerScreen
import com.ataglance.walletglance.account.presentation.screen.EditAccountScreen
import com.ataglance.walletglance.account.presentation.screen.EditAccountsScreen
import com.ataglance.walletglance.account.presentation.viewmodel.CurrencyPickerViewModel
import com.ataglance.walletglance.account.presentation.viewmodel.CurrencyPickerViewModelFactory
import com.ataglance.walletglance.account.presentation.viewmodel.EditAccountViewModel
import com.ataglance.walletglance.account.presentation.viewmodel.EditAccountsViewModel
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import kotlinx.coroutines.launch

fun NavGraphBuilder.accountsGraph(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    navViewModel: NavigationViewModel,
    appConfiguration: AppConfiguration
) {
    navigation<SettingsScreens.Accounts>(startDestination = AccountsSettingsScreens.EditAccounts) {
        composable<AccountsSettingsScreens.EditAccounts> { backStack ->
            val accountsViewModel = backStack.sharedKoinNavViewModel<EditAccountsViewModel>(navController)
            val accountViewModel = backStack.sharedViewModel<EditAccountViewModel>(navController)

            val accounts by accountsViewModel.accounts.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()

            EditAccountsScreen(
                scaffoldPadding = scaffoldPadding,
                isAppSetUp = appConfiguration.isSetUp,
                accounts = accounts,
                onNavigateToEditAccountScreen = { account ->
                    accountViewModel.applyAccount(
                        account = account ?: accountsViewModel.getNewAccount()
                    )
                    navViewModel.navigateToScreen(
                        navController = navController, screen = AccountsSettingsScreens.EditAccount
                    )
                },
                onMoveAccounts = accountsViewModel::moveAccounts,
                onSaveButton = {
                    coroutineScope.launch {
                        accountsViewModel.saveAccounts()

                        if (appConfiguration.isSetUp) {
                            navController.popBackStack()
                        } else {
                            navViewModel.navigateToScreen(navController, SettingsScreens.Categories)
                        }
                    }
                }
            )
        }
        composable<AccountsSettingsScreens.EditAccount> { backStack ->
            val accountsViewModel = backStack.sharedKoinNavViewModel<EditAccountsViewModel>(navController)
            val accountViewModel = backStack.sharedViewModel<EditAccountViewModel>(navController)

            val accountDraft by accountViewModel.accountDraft.collectAsStateWithLifecycle()
            val allowDeleting by accountsViewModel.allowDeleting.collectAsStateWithLifecycle()
            val allowSaving by accountViewModel.allowSaving.collectAsStateWithLifecycle()

            EditAccountScreen(
                scaffoldPadding = scaffoldPadding,
                accountDraft = accountDraft,
                allowDeleting = allowDeleting,
                allowSaving = allowSaving,
                onColorChange = accountViewModel::changeColor,
                onNameChange = accountViewModel::changeName,
                onNavigateToEditAccountCurrencyScreen = {
                    navViewModel.navigateToScreen(
                        navController = navController,
                        screen = AccountsSettingsScreens.EditAccountCurrency(
                            currency = accountDraft.currency
                        )
                    )
                },
                onBalanceChange = accountViewModel::changeBalance,
                onHideChange = accountViewModel::changeHideStatus,
                onHideBalanceChange = accountViewModel::changeHideBalanceStatus,
                onWithoutBalanceChange = accountViewModel::changeWithoutBalanceStatus,
                onDeleteButton = { accountId ->
                    navController.popBackStack()
                    accountsViewModel.deleteAccount(id = accountId)
                },
                onSaveButton = {
                    accountViewModel.getAccount()?.let(accountsViewModel::applyAccount)
                    navController.popBackStack()
                }
            )
        }
        composable<AccountsSettingsScreens.EditAccountCurrency> { backStack ->
            val currency = backStack.toRoute<AccountsSettingsScreens.EditAccountCurrency>().currency

            val editAccountViewModel = backStack.sharedViewModel<EditAccountViewModel>(navController)
            val currencyPickerViewModel = viewModel<CurrencyPickerViewModel>(
                factory = CurrencyPickerViewModelFactory(selectedCurrency = currency)
            )

            val uiState by currencyPickerViewModel.uiState.collectAsStateWithLifecycle()
            val currencyList by currencyPickerViewModel.currencyList.collectAsStateWithLifecycle()
            val searchedPrompt by currencyPickerViewModel.searchedPrompt.collectAsStateWithLifecycle()

            CurrencyPickerScreen(
                scaffoldPadding = scaffoldPadding,
                currencyPickerUiState = uiState,
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