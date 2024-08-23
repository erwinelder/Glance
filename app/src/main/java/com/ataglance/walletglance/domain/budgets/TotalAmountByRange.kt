package com.ataglance.walletglance.domain.budgets

import com.ataglance.walletglance.domain.date.LongDateRange

data class TotalAmountByRange(
    val dateRange: LongDateRange,
    val totalAmount: Double
)
