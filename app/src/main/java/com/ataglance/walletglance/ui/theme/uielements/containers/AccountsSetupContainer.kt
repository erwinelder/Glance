package com.ataglance.walletglance.ui.theme.uielements.containers

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.ataglance.walletglance.domain.entities.Account
import com.ataglance.walletglance.ui.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.accounts.MediumAccountSetup
import com.ataglance.walletglance.ui.theme.uielements.buttons.SmallPrimaryButton

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColumnScope.AccountsSetupContainer(
    appTheme: AppTheme?,
    accountsList: List<Account>,
    onAddNewAccount: () -> Unit,
    navigateToEditAccountScreen: (Int) -> Unit,
    swapAccounts: (Int, Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        GlassSurface(
            modifier = Modifier.weight(1f),
            filledWidth = if (!WindowTypeIsExpanded) null else .86f
        ) {
            AnimatedContent(
                targetState = accountsList,
                label = "accounts list uploading"
            ) { targetList ->
                if (!WindowTypeIsExpanded) {
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
                            items = targetList,
                            key = { it.orderNum }
                        ) { account ->
                            MediumAccountSetup(
                                account = account,
                                appTheme = appTheme,
                                fontSize = 20, roundedCornerSize = 18.dp,
                                onAccountClick = { navigateToEditAccountScreen(account.orderNum) },
                                upButtonEnabled = account.orderNum > 1,
                                onUpButtonClick = { swapAccounts(account.orderNum, account.orderNum - 1) },
                                downButtonEnabled = account.orderNum < targetList.size,
                                onDownButtonClick = { swapAccounts(account.orderNum, account.orderNum + 1) }
                            )
                        }
                    }
                } else {
                    val scrollState = rememberScrollState()
                    FlowRow(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .fillMaxWidth()
                            .padding(9.dp)
                    ) {
                        targetList.forEach { account ->
                            Box(modifier = Modifier.padding(9.dp)) {
                                MediumAccountSetup(
                                    account = account,
                                    appTheme = appTheme,
                                    fontSize = 20, roundedCornerSize = 18.dp,
                                    onAccountClick = { navigateToEditAccountScreen(account.orderNum) },
                                    upButtonEnabled = account.orderNum > 1,
                                    onUpButtonClick = { swapAccounts(account.orderNum, account.orderNum - 1) },
                                    downButtonEnabled = account.orderNum < targetList.size,
                                    onDownButtonClick = { swapAccounts(account.orderNum, account.orderNum + 1) }
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.buttons_gap)))
        SmallPrimaryButton(
            onClick = { onAddNewAccount() },
            text = stringResource(R.string.add_account)
        )
    }
}