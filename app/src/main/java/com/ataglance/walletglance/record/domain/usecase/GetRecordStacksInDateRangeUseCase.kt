package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.record.domain.model.RecordStack
import kotlinx.coroutines.flow.Flow

interface GetRecordStacksInDateRangeUseCase {

    fun getFlow(range: LongDateRange): Flow<List<RecordStack>>

    suspend fun get(range: LongDateRange): List<RecordStack>

}