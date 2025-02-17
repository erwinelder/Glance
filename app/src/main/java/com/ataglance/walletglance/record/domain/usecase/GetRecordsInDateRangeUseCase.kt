package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.record.domain.model.Record
import kotlinx.coroutines.flow.Flow

interface GetRecordsInDateRangeUseCase {
    fun getFlow(range: LongDateRange): Flow<List<Record>>
    suspend fun get(range: LongDateRange): List<Record>
}