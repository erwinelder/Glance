package com.ataglance.walletglance.transaction.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.core.domain.statistics.TotalAmountInRange
import kotlinx.coroutines.flow.Flow

interface GetTotalExpensesInDateRangesUseCase {

    fun getByCategoryAndAccounts(
        categoryId: Int,
        accountIds: List<Int>,
        dateRanges: List<TimestampRange>
    ): Flow<List<TotalAmountInRange>>

}