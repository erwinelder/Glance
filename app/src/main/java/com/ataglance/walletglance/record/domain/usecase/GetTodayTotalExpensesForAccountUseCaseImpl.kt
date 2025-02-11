package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.core.utils.getTodayLongDateRange
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.data.utils.getTotalAmountByType
import kotlinx.coroutines.flow.firstOrNull

class GetTodayTotalExpensesForAccountUseCaseImpl(
    private val recordRepository: RecordRepository
) : GetTodayTotalExpensesForAccountUseCase {
    override suspend fun execute(accountId: Int): Double {
        return recordRepository
            .getRecordsInDateRange(range = getTodayLongDateRange()).firstOrNull()
            ?.filter { it.accountId == accountId }
            ?.getTotalAmountByType(CategoryType.Expense)
            ?: 0.0
    }
}