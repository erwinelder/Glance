package com.ataglance.walletglance.budget.domain

import com.ataglance.walletglance.core.domain.date.LongDateRange

data class TotalAmountByRange(
    val dateRange: LongDateRange,
    val totalAmount: Double
)
