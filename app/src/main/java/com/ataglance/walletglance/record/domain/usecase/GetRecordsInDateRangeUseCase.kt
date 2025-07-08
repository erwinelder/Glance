package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems
import kotlinx.coroutines.flow.Flow

interface GetRecordsInDateRangeUseCase {

    fun getAsFlow(range: TimestampRange): Flow<List<RecordWithItems>>

    suspend fun get(range: TimestampRange): List<RecordWithItems>

}