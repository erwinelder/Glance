package com.ataglance.walletglance.record.domain.model

import com.ataglance.walletglance.core.data.model.LongDateRange
import com.ataglance.walletglance.record.data.model.RecordEntity

data class RecordsInDateRange(
    val dateRange: LongDateRange = LongDateRange(0, 0),
    val recordList: List<RecordEntity> = emptyList()
)
