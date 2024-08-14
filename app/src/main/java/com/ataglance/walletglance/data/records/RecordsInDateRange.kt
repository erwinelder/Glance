package com.ataglance.walletglance.data.records

import com.ataglance.walletglance.data.date.LongDateRange
import com.ataglance.walletglance.domain.entities.Record

data class RecordsInDateRange(
    val dateRange: LongDateRange = LongDateRange(0, 0),
    val recordList: List<Record> = emptyList()
)
