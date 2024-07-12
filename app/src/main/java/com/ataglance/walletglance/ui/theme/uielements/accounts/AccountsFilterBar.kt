package com.ataglance.walletglance.ui.theme.uielements.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.app.AppTheme

@Composable
fun AccountsFilterBar(
    accountList: List<Account>,
    appTheme: AppTheme?,
    onAccountClick: (Int) -> Unit
) {
    val lazyListState = rememberLazyListState()
    var finalAccountList by remember { mutableStateOf(accountList.filterNot { it.hide }) }
    LaunchedEffect(key1 = accountList) {
        finalAccountList = accountList.filterNot { it.hide }
    }

    LazyRow(
        state = lazyListState,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Spacer(modifier = Modifier.width(16.dp))
        }
        items(items = finalAccountList) { account ->
            SmallAccount(
                account = account,
                appTheme = appTheme,
                fontSize = 20.sp,
                roundedCornerSize = 16.dp,
                horizontalPadding = 12.dp,
                verticalPadding = 6.dp,
                outerPadding = PaddingValues(horizontal = 3.dp),
                adjustStyleByActiveStatus = true,
                showBalance = false,
                onClick = { onAccountClick(account.orderNum) }
            )
        }
        item {
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}