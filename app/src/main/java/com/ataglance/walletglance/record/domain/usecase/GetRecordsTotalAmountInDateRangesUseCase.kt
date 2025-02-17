package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.domain.statistics.TotalAmountInRange
import kotlinx.coroutines.flow.Flow

interface GetRecordsTotalAmountInDateRangesUseCase {

    fun getByCategoryAndAccountsFlow(
        categoryId: Int,
        accountsIds: List<Int>,
        dateRangeList: List<LongDateRange>
    ): Flow<List<TotalAmountInRange>>

}