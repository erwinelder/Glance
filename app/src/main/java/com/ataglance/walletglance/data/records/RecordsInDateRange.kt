package com.ataglance.walletglance.data.records

import com.ataglance.walletglance.data.date.LongDateRange
import com.ataglance.walletglance.data.local.entities.RecordEntity

data class RecordsInDateRange(
    val dateRange: LongDateRange = LongDateRange(0, 0),
    val recordList: List<RecordEntity> = emptyList()
)
