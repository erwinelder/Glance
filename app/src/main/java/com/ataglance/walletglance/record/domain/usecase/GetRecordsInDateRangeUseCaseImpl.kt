package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.domain.model.Record
import com.ataglance.walletglance.record.mapper.toDomainModels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class GetRecordsInDateRangeUseCaseImpl(
    private val recordRepository: RecordRepository
) : GetRecordsInDateRangeUseCase {

    override fun getAsFlow(range: LongDateRange): Flow<List<Record>> {
        return recordRepository.getRecordsInDateRange(range = range).map { records ->
            records.toDomainModels()
        }
    }

    override suspend fun get(range: LongDateRange): List<Record> {
        return recordRepository.getRecordsInDateRange(range = range)
            .firstOrNull().orEmpty()
            .toDomainModels()
    }

}