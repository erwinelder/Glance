package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.record.domain.model.RecordStack
import kotlinx.coroutines.flow.Flow

interface GetRecordStacksInDateRangeUseCase {

    fun getFlow(range: TimestampRange): Flow<List<RecordStack>>

    suspend fun get(range: TimestampRange): List<RecordStack>

}