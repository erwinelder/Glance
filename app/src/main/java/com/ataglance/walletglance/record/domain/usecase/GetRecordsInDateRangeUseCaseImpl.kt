package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.domain.model.Record
import com.ataglance.walletglance.record.mapper.toDomainModels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRecordsInDateRangeUseCaseImpl(
    private val recordRepository: RecordRepository
) : GetRecordsInDateRangeUseCase {

    override fun getFlow(range: TimestampRange): Flow<List<Record>> {
        return recordRepository.getRecordsInDateRangeFlow(range = range).map { records ->
            records.toDomainModels()
        }
    }

    override suspend fun get(range: TimestampRange): List<Record> {
        return recordRepository.getRecordsInDateRange(range = range).toDomainModels()
    }

}