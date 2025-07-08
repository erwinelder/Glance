package com.ataglance.walletglance.core.domain.statistics

import com.ataglance.walletglance.core.domain.date.TimestampRange

data class TotalAmountInRange(
    val dateRange: TimestampRange,
    val totalAmount: Double
)
