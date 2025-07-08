package com.ataglance.walletglance.transaction.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.core.domain.statistics.TotalAmountInRange
import com.ataglance.walletglance.record.domain.usecase.GetRecordsTotalExpensesInDateRange
import com.ataglance.walletglance.transfer.domain.usecase.GetTransfersTotalExpensesInDateRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTotalExpensesInDateRangesUseCaseImpl(
    private val getRecordsTotalExpensesInDateRange: GetRecordsTotalExpensesInDateRange,
    private val getTransfersTotalExpensesInDateRange: GetTransfersTotalExpensesInDateRange
) : GetTotalExpensesInDateRangesUseCase {

    override fun getByCategoryAndAccounts(
        categoryId: Int,
        accountIds: List<Int>,
        dateRanges: List<TimestampRange>
    ): Flow<List<TotalAmountInRange>> = flow {
        val totalByRanges = dateRanges
            .map { dateRange ->
                TotalAmountInRange(dateRange = dateRange, totalAmount = 0.0)
            }
            .toMutableList()
        emit(totalByRanges)

        dateRanges.forEachIndexed { index, dateRange ->
            val recordsTotalAmount = getRecordsTotalExpensesInDateRange.getByAccountsAndCategory(
                dateRange = dateRange, accountIds = accountIds, categoryId = categoryId
            )
            val transfersTotalAmount = getTransfersTotalExpensesInDateRange.getByAccountsAndCategory(
                dateRange = dateRange, accountIds = accountIds, categoryId = categoryId
            )

            totalByRanges[index] = TotalAmountInRange(
                dateRange = dateRange, totalAmount = recordsTotalAmount + transfersTotalAmount
            )
            emit(totalByRanges)
        }
    }

}