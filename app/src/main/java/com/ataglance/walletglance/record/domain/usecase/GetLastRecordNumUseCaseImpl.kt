package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.record.data.repository.RecordRepository
import kotlinx.coroutines.flow.Flow

class GetLastRecordNumUseCaseImpl(
    private val recordRepository: RecordRepository
) : GetLastRecordNumUseCase {

    override fun getFlow(): Flow<Int?> {
        return recordRepository.getLastRecordNumFlow()
    }

    override suspend fun get(): Int? {
        return recordRepository.getLastRecordNum()
    }

    override suspend fun getNext(): Int {
        return get()?.plus(1) ?: 1
    }

}