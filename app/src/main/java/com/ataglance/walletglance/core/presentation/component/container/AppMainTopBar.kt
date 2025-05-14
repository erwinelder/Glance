package com.ataglance.walletglance.core.presentation.component.container

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.presentation.components.AccountsFilterBar
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.presentation.theme.GlanceColors

@Composable
fun AppMainTopBar(
    accountList: List<Account>,
    currentDateRangeEnum: DateRangeEnum,
    isCustomDateRangeWindowOpened: Boolean,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    onCustomDateRangeButtonClick: () -> Unit,
    onAccountClick: (Int) -> Unit
) {
    val visibleAccounts by remember(accountList) {
        derivedStateOf { accountList.filterNot { it.hide } }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(GlanceColors.surface)
            .fillMaxWidth()
            .padding(
                top = if (visibleAccounts.size > 1) 10.dp else 12.dp, bottom = 12.dp
            )
    ) {
        if (visibleAccounts.size > 1) {
            AccountsFilterBar(
                visibleAccounts = visibleAccounts,
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