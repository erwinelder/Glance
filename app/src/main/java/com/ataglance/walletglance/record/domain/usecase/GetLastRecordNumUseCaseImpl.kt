package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.record.data.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class GetLastRecordNumUseCaseImpl(
    private val recordRepository: RecordRepository
) : GetLastRecordNumUseCase {

    override fun getAsFlow(): Flow<Int?> {
        return recordRepository.getLastRecordNum()
    }

    override suspend fun get(): Int? {
        return recordRepository.getLastRecordNum().firstOrNull()
    }

}