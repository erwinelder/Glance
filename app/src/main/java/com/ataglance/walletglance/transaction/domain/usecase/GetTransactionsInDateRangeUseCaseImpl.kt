package com.ataglance.walletglance.transaction.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.record.domain.usecase.GetRecordsInDateRangeUseCase
import com.ataglance.walletglance.transaction.domain.model.Transaction
import com.ataglance.walletglance.transfer.domain.usecase.GetTransfersInDateRangeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTransactionsInDateRangeUseCaseImpl(
    private val getRecordsInDateRangeUseCase: GetRecordsInDateRangeUseCase,
    private val getTransfersInDateRangeUseCase: GetTransfersInDateRangeUseCase
) : GetTransactionsInDateRangeUseCase {

    override fun getAsFlow(range: TimestampRange): Flow<List<Transaction>> {
        return combine(
            getRecordsInDateRangeUseCase.getAsFlow(range = range),
            getTransfersInDateRangeUseCase.getAsFlow(range = range)
        ) { records, transfers ->
            (records + transfers).sortedBy { it.date }
        }
    }

    override suspend fun get(range: TimestampRange): List<Transaction> {
        val records = getRecordsInDateRangeUseCase.get(range = range)
        val transfers = getTransfersInDateRangeUseCase.get(range = range)
        return (records + transfers).sortedBy { it.date }
    }

}