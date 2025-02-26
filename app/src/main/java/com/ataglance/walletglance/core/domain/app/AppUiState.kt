package com.ataglance.walletglance.core.domain.app

import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState

data class AppUiState(
    val accountsAndActiveOne: AccountsAndActiveOne,
    val dateRangeMenuUiState: DateRangeMenuUiState
)
