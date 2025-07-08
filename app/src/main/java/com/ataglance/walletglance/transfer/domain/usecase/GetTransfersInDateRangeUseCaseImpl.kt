package com.ataglance.walletglance.transfer.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.transfer.data.repository.TransferRepository
import com.ataglance.walletglance.transaction.domain.model.Transfer
import com.ataglance.walletglance.transfer.mapper.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTransfersInDateRangeUseCaseImpl(
    private val transferRepository: TransferRepository
) : GetTransfersInDateRangeUseCase {

    override fun getAsFlow(range: TimestampRange): Flow<List<Transfer>> {
        return transferRepository
            .getTransfersInDateRangeAsFlow(from = range.from, to = range.to)
            .map { transfers ->
                transfers.map { it.toDomainModel() }
            }
    }

    override suspend fun get(range: TimestampRange): List<Transfer> {
        return transferRepository
            .getTransfersInDateRange(from = range.from, to = range.to)
            .map { it.toDomainModel() }
    }

}