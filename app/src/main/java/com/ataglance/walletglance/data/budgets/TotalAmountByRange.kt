package com.ataglance.walletglance.data.budgets

import com.ataglance.walletglance.data.date.LongDateRange

data class TotalAmountByRange(
    val dateRange: LongDateRange,
    val totalAmount: Double
)
