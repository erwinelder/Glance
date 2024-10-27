package com.ataglance.walletglance.record.domain

import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.record.data.model.RecordEntity

data class RecordsInDateRange(
    val dateRange: LongDateRange = LongDateRange(0, 0),
    val recordList: List<RecordEntity> = emptyList()
)
