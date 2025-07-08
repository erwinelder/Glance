package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.transfer.data.repository.TransferRepository

class GetTransfersTotalExpensesInDateRangeImpl(
    private val transferRepository: TransferRepository
) : GetTransfersTotalExpensesInDateRange {

    override suspend fun getByAccountsAndCategory(
        dateRange: TimestampRange,
        accountIds: List<Int>,
        categoryId: Int
    ): Double {
        if (categoryId != 12 && categoryId != 67) return 0.0

        return transferRepository.getTotalExpensesInDateRangeByAccounts(
            from = dateRange.from, to = dateRange.to, accountIds = accountIds
        )
    }

}