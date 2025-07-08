package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.mapper.toDataModelWithItems
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems

class SaveRecordsUseCaseImpl(
    private val recordRepository: RecordRepository
) : SaveRecordsUseCase {

    override suspend fun execute(recordWithItems: List<RecordWithItems>) {
        recordRepository.upsertRecordsWithItems(
            recordsWithItems = recordWithItems.map { it.toDataModelWithItems() }
        )
    }

}