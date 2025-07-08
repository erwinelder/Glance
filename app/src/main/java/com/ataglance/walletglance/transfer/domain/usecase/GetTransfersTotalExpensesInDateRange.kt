package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange

interface GetTransfersTotalExpensesInDateRange {

    suspend fun getByAccountsAndCategory(
        dateRange: TimestampRange,
        accountIds: List<Int>,
        categoryId: Int
    ): Double

}