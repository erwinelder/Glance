package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.core.domain.statistics.TotalAmountInRange
import kotlinx.coroutines.flow.Flow

interface GetRecordsTotalAmountInDateRangesUseCase {

    fun getFlowByCategoryAndAccounts(
        categoryId: Int,
        accountsIds: List<Int>,
        dateRanges: List<TimestampRange>
    ): Flow<List<TotalAmountInRange>>

}