package com.ataglance.walletglance.core.domain.app

import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.core.domain.date.DateRangeWithEnum

data class AppUiState(
    val accountsAndActiveOne: AccountsAndActiveOne,
    val dateRangeWithEnum: DateRangeWithEnum
)
