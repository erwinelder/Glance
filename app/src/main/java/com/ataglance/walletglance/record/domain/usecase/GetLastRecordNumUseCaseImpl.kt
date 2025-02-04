package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.record.data.repository.RecordRepository
import kotlinx.coroutines.flow.Flow

class GetLastRecordNumUseCaseImpl(
    private val recordRepository: RecordRepository
) : GetLastRecordNumUseCase {
    override fun execute(): Flow<Int?> {
        return recordRepository.getLastRecordNum()
    }
}