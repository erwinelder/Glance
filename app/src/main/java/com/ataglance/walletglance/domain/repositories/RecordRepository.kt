package com.ataglance.walletglance.domain.repositories

import com.ataglance.walletglance.data.date.DateRangeState
import com.ataglance.walletglance.domain.dao.RecordDao
import com.ataglance.walletglance.domain.entities.Record
import com.ataglance.walletglance.data.utils.getTodayDateLong
import kotlinx.coroutines.flow.Flow

class RecordRepository(
    private val dao: RecordDao
) {

    fun getLastRecordNum(): Flow<Int?> {
        return dao.getLastRecordOrderNum()
    }

    fun getAllRecords(): Flow<List<Record>> {
        return dao.getAllRecords()
    }

    fun getRecordsInDateRange(dateRangeState: DateRangeState): Flow<List<Record>> {
        val today = getTodayDateLong()
        return dao.getRecordsInDateRange(
            today, today + 2359,
            dateRangeState.fromPast, dateRangeState.toFuture
        )
    }

}