package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems
import com.ataglance.walletglance.record.mapper.toDomainModelWithItems

class GetRecordUseCaseImpl(
    private val recordRepository: RecordRepository
) : GetRecordUseCase {

    override suspend fun get(id: Long): RecordWithItems? {
        return recordRepository.getRecordWithItems(id = id)?.toDomainModelWithItems()
    }

}