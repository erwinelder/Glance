package com.ataglance.walletglance.account.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.color.AccountPossibleColors
import com.ataglance.walletglance.account.presentation.components.EditingAccountComponent
import com.ataglance.walletglance.account.utils.toAccountColorWithName
import com.ataglance.walletglance.core.domain.app.AppTheme
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
    onSwapAccounts: (Int, Int) -> Unit,
    onSaveButton: () -> Unit
) {
    GlassSurfaceContainer(
        topPadding = if (!isAppSetUp) scaffoldPadding.calculateTopPadding() else null,
        glassSurfaceContent = {
            GlassSurfaceContent(
                accountsList = accountList,
                onNavigateToEditAccountScreen = onNavigateToEditAccountScreen,
                onSwapAccounts = onSwapAccounts
            )
        },
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
    onSwapAccounts: (Int, Int) -> Unit
) {
    AnimatedContent(
        targetState = accountsList,
        label = "accounts list uploading"
    ) { targetList ->
        if (!WindowTypeIsExpanded) {
            CompactLayout(
                accountsList = targetList,
                onNavigateToEditAccountScreen = onNavigateToEditAccountScreen,
                onSwapAccounts = onSwapAccounts
            )
        } else {
            ExpandedLayout(
                accountsList = targetList,
                onNavigateToEditAccountScreen = onNavigateToEditAccountScreen,
                onSwapAccounts = onSwapAccounts
            )
        }
    }
}

@Composable
private fun CompactLayout(
    accountsList: List<Account>,
    onNavigateToEditAccountScreen: (Account) -> Unit,
    onSwapAccounts: (Int, Int) -> Unit
) {
    val lazyColumnState = rememberLazyListState()
    LazyColumn(
        state = lazyColumnState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.widget_content_padding)),
        contentPadding = PaddingValues(dimensionResource(R.dimen.widget_content_padding)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        items(
            items = accountsList,
            key = { it.orderNum }
        ) { account ->
            EditingAccountComponent(
                account = account,
                onAccountClick = onNavigateToEditAccountScreen,
                upButtonEnabled = account.orderNum > 1,
                onUpButtonClick = { onSwapAccounts(account.orderNum, account.orderNum - 1) },
                downButtonEnabled = account.orderNum < accountsList.size,
                onDownButtonClick = { onSwapAccounts(account.orderNum, account.orderNum + 1) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpandedLayout(
    accountsList: List<Account>,
    onNavigateToEditAccountScreen: (Account) -> Unit,
    onSwapAccounts: (Int, Int) -> Unit
) {
    val scrollState = rememberScrollState()

    FlowRow(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(9.dp)
    ) {
        accountsList.forEach { account ->
            Box(modifier = Modifier.padding(9.dp)) {
                EditingAccountComponent(
                    account = account,
                    onAccountClick = onNavigateToEditAccountScreen,
                    upButtonEnabled = account.orderNum > 1,
                    onUpButtonClick = { onSwapAccounts(account.orderNum, account.orderNum - 1) },
                    downButtonEnabled = account.orderNum < accountsList.size,
                    onDownButtonClick = { onSwapAccounts(account.orderNum, account.orderNum + 1) }
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
            onSwapAccounts = { _, _ -> },
            onSaveButton = {}
        )
    }
}
