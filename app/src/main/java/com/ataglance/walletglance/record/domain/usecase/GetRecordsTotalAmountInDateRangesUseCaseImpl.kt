package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.core.domain.statistics.TotalAmountInRange
import com.ataglance.walletglance.record.data.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRecordsTotalAmountInDateRangesUseCaseImpl(
    private val recordRepository: RecordRepository
) : GetRecordsTotalAmountInDateRangesUseCase {

    override fun getFlowByCategoryAndAccounts(
        categoryId: Int,
        accountsIds: List<Int>,
        dateRanges: List<TimestampRange>
    ): Flow<List<TotalAmountInRange>> = flow {
        val initialList = dateRanges.map { dateRange ->
            TotalAmountInRange(dateRange = dateRange, totalAmount = 0.0)
        }
        emit(initialList)

        val totalByRanges = initialList.toMutableList()

        dateRanges.forEachIndexed { index, dateRange ->
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