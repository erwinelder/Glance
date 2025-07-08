package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.record.data.repository.RecordRepository

class GetRecordsTotalExpensesInDateRangeImpl(
    private val recordRepository: RecordRepository
): GetRecordsTotalExpensesInDateRange {

    override suspend fun getByAccountsAndCategory(
        dateRange: TimestampRange,
        accountIds: List<Int>,
        categoryId: Int
    ): Double {
        return recordRepository.getTotalExpensesInDateRangeByAccountsAndCategory(
            dateRange = dateRange, accountIds = accountIds, categoryId = categoryId
        )
    }

}