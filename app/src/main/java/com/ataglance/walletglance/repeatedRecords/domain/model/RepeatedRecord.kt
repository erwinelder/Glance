package com.ataglance.walletglance.repeatedRecords.domain.model

import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.record.domain.model.RecordType

data class RepeatedRecord(
    val id: Int,
    val repeatedPeriod: RepeatingPeriod,
    val defaultDate: Long,
    val type: RecordType,
    val accountId: Int,
    val amount: Double,
    val quantity: Int?,
    val categoryId: Int,
    val subcategoryId: Int?,
    val note: String?,
    val includeInBudgets: Boolean
)
