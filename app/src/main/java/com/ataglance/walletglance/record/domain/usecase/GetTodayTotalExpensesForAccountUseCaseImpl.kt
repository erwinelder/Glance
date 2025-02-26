package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.data.utils.getTotalAmountByType

class GetTodayTotalExpensesForAccountUseCaseImpl(
    private val recordRepository: RecordRepository
) : GetTodayTotalExpensesForAccountUseCase {
    override suspend fun get(accountId: Int): Double {
        return recordRepository
            .getRecordsInDateRange(range = LongDateRange.asToday())
            .filter { it.accountId == accountId }
            .getTotalAmountByType(CategoryType.Expense)
    }
}