package com.ataglance.walletglance.account.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.domain.navigation.AccountsSettingsScreens
import com.ataglance.walletglance.account.presentation.component.EditingAccountComponent
import com.ataglance.walletglance.account.presentation.viewmodel.EditAccountViewModel
import com.ataglance.walletglance.account.presentation.viewmodel.EditAccountsViewModel
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.component.screenContainer.GlassSurfaceScreenContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import kotlinx.coroutines.launch

@Composable
fun EditAccountsScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    backStack: NavBackStackEntry,
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appConfiguration: AppConfiguration
) {
    val accountsViewModel = backStack.sharedKoinNavViewModel<EditAccountsViewModel>(navController)
    val accountViewModel = backStack.sharedViewModel<EditAccountViewModel>(navController)

    val accounts by accountsViewModel.accounts.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    EditAccountsScreen(
        screenPadding = screenPadding,
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

@Composable
fun EditAccountsScreen(
    screenPadding: PaddingValues = PaddingValues(),
    isAppSetUp: Boolean,
    accounts: List<Account>,
    onNavigateToEditAccountScreen: (Account?) -> Unit,
    onMoveAccounts: (Int, Int) -> Unit,
    onSaveButton: () -> Unit
) {
    GlassSurfaceScreenContainer(
        topPadding = screenPadding.calculateTopPadding(),
        bottomPadding = screenPadding.calculateBottomPadding(),
        glassSurfaceContent = {
            GlassSurfaceContent(
                accountsList = accounts,
                onNavigateToEditAccountScreen = onNavigateToEditAccountScreen,
                onMoveAccounts = onMoveAccounts
            )
        },
        glassSurfaceFilledWidths = FilledWidthByScreenType(.86f, .86f, .86f),
        smallPrimaryButton = {
            SmallPrimaryButton(
                text = stringResource(R.string.add_account),
                iconRes = R.drawable.add_icon
            ) {
                onNavigateToEditAccountScreen(null)
            }
        },
        primaryBottomButton = {
            PrimaryButton(
                text = stringResource(
                    if (isAppSetUp) R.string.save else R.string.save_and_continue
                ),
                onClick = onSaveButton
            )
        }
    )
}

@Composable
private fun GlassSurfaceContent(
    accountsList: List<Account>,
    onNavigateToEditAccountScreen: (Account) -> Unit,
    onMoveAccounts: (Int, Int) -> Unit
) {
    if (!WindowTypeIsExpanded) {
        CompactLayout(
            accountsList = accountsList,
            onNavigateToEditAccountScreen = onNavigateToEditAccountScreen,
            onMoveAccounts = onMoveAccounts
        )
    } else {
        ExpandedLayout(
            accountsList = accountsList,
            onNavigateToEditAccountScreen = onNavigateToEditAccountScreen,
            onMoveAccounts = onMoveAccounts
        )
    }
}

@Composable
private fun CompactLayout(
    accountsList: List<Account>,
    onNavigateToEditAccountScreen: (Account) -> Unit,
    onMoveAccounts: (Int, Int) -> Unit
) {
    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        state = lazyColumnState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        itemsIndexed(
            items = accountsList,
            key = { _, item -> item.id }
        ) { index, account ->
            EditingAccountComponent(
                account = account,
                onAccountClick = onNavigateToEditAccountScreen,
                upButtonEnabled = index > 0,
                onUpButtonClick = { onMoveAccounts(index, index - 1) },
                downButtonEnabled = index < accountsList.lastIndex,
                onDownButtonClick = { onMoveAccounts(index, index + 1) },
                modifier = Modifier.animateItem()
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpandedLayout(
    accountsList: List<Account>,
    onNavigateToEditAccountScreen: (Account) -> Unit,
    onMoveAccounts: (Int, Int) -> Unit
) {
    val scrollState = rememberScrollState()

    FlowRow(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        accountsList.forEachIndexed { index, account ->
            Box(modifier = Modifier.padding(8.dp)) {
                EditingAccountComponent(
                    account = account,
                    onAccountClick = onNavigateToEditAccountScreen,
                    upButtonEnabled = index > 0,
                    onUpButtonClick = { onMoveAccounts(index, index - 1) },
                    downButtonEnabled = index < accountsList.lastIndex,
                    onDownButtonClick = { onMoveAccounts(index, index + 1) }
                )
            }
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EditAccountsScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    accountList: List<Account> = listOf(
        Account(
            id = 1,
            orderNum = 1,
            name = "Main USD",
            currency = "USD",
            balance = 112.13,
            color = AccountColors.Default,
            isActive = false
        ),
        Account(
            id = 2,
            orderNum = 2,
            name = "Local Card CZK",
            currency = "CZK",
            balance = 1412.13,
            color = AccountColors.Pink,
            isActive = false
        ),
    ),
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        EditAccountsScreen(
            screenPadding = scaffoldPadding,
            isAppSetUp = isAppSetUp,
            accounts = accountList,
            onNavigateToEditAccountScreen = {},
            onMoveAccounts = { _, _ -> },
            onSaveButton = {}
        )
    }
}
