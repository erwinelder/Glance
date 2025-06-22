package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.record.domain.model.Record
import kotlinx.coroutines.flow.Flow

interface GetRecordsInDateRangeUseCase {

    fun getFlow(range: TimestampRange): Flow<List<Record>>

    suspend fun get(range: TimestampRange): List<Record>

}