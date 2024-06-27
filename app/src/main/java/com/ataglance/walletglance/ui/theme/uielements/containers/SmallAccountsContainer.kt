package com.ataglance.walletglance.ui.theme.uielements.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.accounts.SmallAccount

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SmallAccountsContainer(
    accountList: List<Account>,
    appTheme: AppTheme?,
    onAccountClick: (Int) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.screen_horizontal_padding))
    ) {
        accountList.forEach { account ->
            if (!account.hide) {
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
        }
    }
}