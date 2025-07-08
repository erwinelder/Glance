package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems
import com.ataglance.walletglance.record.mapper.toDomainModelWithItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRecordsInDateRangeUseCaseImpl(
    private val recordRepository: RecordRepository
) : GetRecordsInDateRangeUseCase {

    override fun getAsFlow(range: TimestampRange): Flow<List<RecordWithItems>> {
        return recordRepository
            .getRecordsWithItemsInDateRangeAsFlow(from = range.from, to = range.to)
            .map { recordsWithItems ->
                recordsWithItems.mapNotNull { it.toDomainModelWithItems() }
            }
    }

    override suspend fun get(range: TimestampRange): List<RecordWithItems> {
        return recordRepository
            .getRecordsWithItemsInDateRange(from = range.from, to = range.to)
            .mapNotNull { it.toDomainModelWithItems() }
    }

}