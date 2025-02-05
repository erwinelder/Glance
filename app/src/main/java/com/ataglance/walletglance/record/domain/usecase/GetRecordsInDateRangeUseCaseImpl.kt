package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.data.model.LongDateRange
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.domain.model.Record
import com.ataglance.walletglance.record.mapper.toDomainModels
import kotlinx.coroutines.flow.firstOrNull

class GetRecordsInDateRangeUseCaseImpl(
    private val recordRepository: RecordRepository
) : GetRecordsInDateRangeUseCase {
    override suspend fun get(range: LongDateRange): List<Record> {
        return recordRepository.getRecordsInDateRange(range = range)
            .firstOrNull().orEmpty()
            .toDomainModels()
    }
}