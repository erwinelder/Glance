package com.ataglance.walletglance.account.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.account.domain.model.Account

@Composable
fun AccountsFilterBar(
    visibleAccounts: List<Account>,
    onAccountClick: (Int) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyRow(
        state = lazyListState,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Spacer(modifier = Modifier.width(16.dp))
        }
        items(items = visibleAccounts) { account ->
            SmallAccount(
                account = account,
                fontSize = 20.sp,
                roundedCornerSize = 16.dp,
                horizontalPadding = 12.dp,
                verticalPadding = 6.dp,
                outerPadding = PaddingValues(horizontal = 3.dp),
                adjustStyleByActiveStatus = true,
                showBalance = false,
                onClick = { onAccountClick(account.id) }
            )
        }
        item {
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}