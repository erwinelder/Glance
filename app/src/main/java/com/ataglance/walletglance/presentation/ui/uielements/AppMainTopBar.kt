package com.ataglance.walletglance.presentation.ui.uielements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.date.DateRangeEnum
import com.ataglance.walletglance.presentation.ui.GlanceTheme
import com.ataglance.walletglance.presentation.ui.uielements.containers.DateFilterBar
import com.ataglance.walletglance.presentation.ui.uielements.accounts.AccountsFilterBar

@Composable
fun AppMainTopBar(
    accountList: List<Account>,
    currentDateRangeEnum: DateRangeEnum,
    isCustomDateRangeWindowOpened: Boolean,
    appTheme: AppTheme?,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    onCustomDateRangeButtonClick: () -> Unit,
    onAccountClick: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(GlanceTheme.surface)
            .fillMaxWidth()
            .padding(
                top = if (accountList.size > 1) 10.dp else 12.dp, bottom = 12.dp
            )
    ) {
        if (accountList.size > 1) {
            AccountsFilterBar(
                accountList = accountList,
                appTheme = appTheme,
                onAccountClick = onAccountClick
            )
        }
        DateFilterBar(
            currentDateRangeEnum = currentDateRangeEnum,
            isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
            onDateRangeChange = onDateRangeChange,
            onCustomDateRangeButtonClick = onCustomDateRangeButtonClick
        )
    }
}