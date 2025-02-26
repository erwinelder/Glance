package com.ataglance.walletglance.core.domain.statistics

import com.ataglance.walletglance.core.domain.date.LongDateRange

data class TotalAmountInRange(
    val dateRange: LongDateRange,
    val totalAmount: Double
)
