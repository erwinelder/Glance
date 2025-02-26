package com.ataglance.walletglance.record.data.model

import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.record.domain.model.RecordStack

data class RecordsInDateRange(
    val dateRange: LongDateRange = LongDateRange(0, 0),
    val recordStacks: List<RecordStack> = emptyList()
)
