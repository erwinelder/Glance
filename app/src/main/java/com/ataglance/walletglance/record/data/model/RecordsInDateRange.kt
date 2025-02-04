package com.ataglance.walletglance.record.data.model

import com.ataglance.walletglance.core.data.model.LongDateRange
import com.ataglance.walletglance.record.data.local.model.RecordEntity

data class RecordsInDateRange(
    val dateRange: LongDateRange = LongDateRange(0, 0),
    val recordList: List<RecordEntity> = emptyList()
)
