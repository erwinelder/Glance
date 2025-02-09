package com.ataglance.walletglance.record.data.model

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.record.data.local.model.RecordEntity

data class DataAfterRecordOperation(
    val recordsDelete: List<RecordEntity> = emptyList(),
    val recordsToUpsert: List<RecordEntity> = emptyList(),
    val accountsToUpsert: List<AccountEntity> = emptyList()
)
