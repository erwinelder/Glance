package com.ataglance.walletglance.record.domain.usecase

import com.ataglance.walletglance.core.data.model.LongDateRange
import com.ataglance.walletglance.record.domain.model.Record

interface GetRecordsInDateRangeUseCase {
    suspend fun get(range: LongDateRange): List<Record>
}