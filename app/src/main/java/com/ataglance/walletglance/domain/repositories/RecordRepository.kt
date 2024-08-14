package com.ataglance.walletglance.domain.repositories

import com.ataglance.walletglance.data.date.LongDateRange
import com.ataglance.walletglance.data.utils.getTodayLongDateRange
import com.ataglance.walletglance.domain.dao.RecordDao
import com.ataglance.walletglance.domain.entities.Record
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

    fun getRecordsForToday(): Flow<List<Record>> {
        val todayDateRange = getTodayLongDateRange()
        return dao.getRecordsInDateRange(todayDateRange.from, todayDateRange.to)
    }

    fun getRecordsInDateRange(longDateRange: LongDateRange): Flow<List<Record>> {
        return dao.getRecordsInDateRange(longDateRange.from, longDateRange.to)
    }

}