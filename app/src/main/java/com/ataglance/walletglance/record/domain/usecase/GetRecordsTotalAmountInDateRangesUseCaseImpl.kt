package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.domain.statistics.TotalAmountInRange
import com.ataglance.walletglance.record.data.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRecordsTotalAmountInDateRangesUseCaseImpl(
    private val recordRepository: RecordRepository
) : GetRecordsTotalAmountInDateRangesUseCase {

    override fun getByCategoryAndAccountsAsFlow(
        categoryId: Int,
        accountsIds: List<Int>,
        dateRangeList: List<LongDateRange>
    ): Flow<List<TotalAmountInRange>> = flow {
        val initialList = dateRangeList.map { TotalAmountInRange(it, 0.0) }
        emit(initialList)

        val totalByRanges = initialList.toMutableList()

        dateRangeList.forEachIndexed { index, dateRange ->
            val totalAmount = recordRepository.getTotalAmountByCategoryAndAccountsInRange(
                categoryId = categoryId,
                accountsIds = accountsIds,
                dateRange = dateRange
            )

            totalByRanges[index] = TotalAmountInRange(
                dateRange = dateRange, totalAmount = totalAmount
            )
            emit(totalByRanges)
        }
    }

}