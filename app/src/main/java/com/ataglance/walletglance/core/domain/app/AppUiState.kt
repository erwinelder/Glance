package com.ataglance.walletglance.core.domain.app

import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState
import com.ataglance.walletglance.record.domain.model.RecordStack

data class AppUiState(
    val dateRangeMenuUiState: DateRangeMenuUiState,
    val accountsAndActiveOne: AccountsAndActiveOne,
    val recordStackListByDate: List<RecordStack>
)
