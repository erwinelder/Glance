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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.color.AccountPossibleColors
import com.ataglance.walletglance.account.presentation.components.EditingAccountComponent
import com.ataglance.walletglance.account.domain.utils.toAccountColorWithName
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.WindowTypeIsExpanded
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceContainer

@Composable
fun EditAccountsScreen(
    scaffoldPadding: PaddingValues,
    isAppSetUp: Boolean,
    accountList: List<Account>,
    onNavigateToEditAccountScreen: (Account?) -> Unit,
    onMoveAccounts: (Int, Int) -> Unit,
    onSaveButton: () -> Unit
) {
    GlassSurfaceContainer(
        topPadding = scaffoldPadding.takeUnless { isAppSetUp }?.calculateTopPadding(),
        glassSurfaceContent = {
            GlassSurfaceContent(
                accountsList = accountList,
                onNavigateToEditAccountScreen = onNavigateToEditAccountScreen,
                onMoveAccounts = onMoveAccounts
            )
        },
        glassSurfaceFilledWidths = FilledWidthByScreenType(.86f, .86f, .86f),
        smallPrimaryButton = {
            SmallPrimaryButton(text = stringResource(R.string.add_account)) {
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
    isSetupProgressTopBarVisible: Boolean = false,
    accountList: List<Account> = listOf(
        Account(
            id = 1,
            orderNum = 1,
            name = "Main USD",
            currency = "USD",
            balance = 112.13,
            color = AccountPossibleColors().default.toAccountColorWithName(),
            isActive = false
        ),
        Account(
            id = 2,
            orderNum = 2,
            name = "Local Card CZK",
            currency = "CZK",
            balance = 1412.13,
            color = AccountPossibleColors().pink.toAccountColorWithName(),
            isActive = false
        ),
    ),
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
    ) { scaffoldPadding ->
        EditAccountsScreen(
            scaffoldPadding = scaffoldPadding,
            isAppSetUp = isAppSetUp,
            accountList = accountList,
            onNavigateToEditAccountScreen = {},
            onMoveAccounts = { _, _ -> },
            onSaveButton = {}
        )
    }
}
