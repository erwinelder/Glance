package com.ataglance.walletglance.presentation.ui.screens.settings.accounts

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
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.presentation.ui.WindowTypeIsExpanded
import com.ataglance.walletglance.presentation.ui.screencontainers.SetupDataScreenContainer
import com.ataglance.walletglance.presentation.ui.uielements.accounts.MediumAccountSetup
import com.ataglance.walletglance.presentation.ui.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.presentation.ui.uielements.buttons.SmallPrimaryButton

@Composable
fun EditAccountsScreen(
    scaffoldPadding: PaddingValues,
    isAppSetUp: Boolean,
    appTheme: AppTheme?,
    accountsList: List<Account>,
    onNavigateToEditAccountScreen: (Account?) -> Unit,
    onSwapAccounts: (Int, Int) -> Unit,
    onSaveButton: () -> Unit
) {
    SetupDataScreenContainer(
        topPadding = if (!isAppSetUp) scaffoldPadding.calculateTopPadding() else null,
        glassSurfaceContent = {
            GlassSurfaceContent(
                accountsList = accountsList,
                appTheme = appTheme,
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
    appTheme: AppTheme?,
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
                appTheme = appTheme,
                onNavigateToEditAccountScreen = onNavigateToEditAccountScreen,
                onSwapAccounts = onSwapAccounts
            )
        } else {
            ExpandedLayout(
                accountsList = targetList,
                appTheme = appTheme,
                onNavigateToEditAccountScreen = onNavigateToEditAccountScreen,
                onSwapAccounts = onSwapAccounts
            )
        }
    }
}

@Composable
private fun CompactLayout(
    accountsList: List<Account>,
    appTheme: AppTheme?,
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
            MediumAccountSetup(
                account = account,
                appTheme = appTheme,
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
    appTheme: AppTheme?,
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
                MediumAccountSetup(
                    account = account,
                    appTheme = appTheme,
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