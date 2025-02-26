package com.ataglance.walletglance.recordCreation.domain.record

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.category.domain.model.CategoryType

data class CreatedRecord(
    val isNew: Boolean,
    val recordNum: Int,
    val account: Account,
    val type: CategoryType,
    val dateLong: Long,
    val preferences: RecordDraftPreferences,

    val items: List<CreatedRecordItem>,
    val totalAmount: Double
)
