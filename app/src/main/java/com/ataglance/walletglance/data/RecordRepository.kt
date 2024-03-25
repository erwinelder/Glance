package com.ataglance.walletglance.data

import com.ataglance.walletglance.model.DateRangeController
import com.ataglance.walletglance.model.DateRangeState
import kotlinx.coroutines.flow.Flow

class RecordRepository(
    private val dao: RecordDao
) {

    suspend fun deleteAllRecords() {
        dao.deleteAllRecords()
    }

    fun getLastRecordNum(): Flow<Int?> {
        return dao.getLastRecordOrderNum()
    }

    fun getAllRecords(): Flow<List<Record>> {
        return dao.getAllRecords()
    }

    fun getRecordsInDateRange(dateRangeState: DateRangeState): Flow<List<Record>> {
        val today = DateRangeController().getTodayDateLong()
        return dao.getRecordsInDateRange(
            today, today + 2359,
            dateRangeState.fromPast, dateRangeState.toFuture
        )
    }

    fun getTransfersByAccountId(accountId: Int): Flow<List<Record>> {
        return dao.getTransfersByAccountId(accountId)
    }

    fun getRecordsByRecordNumbers(recordNumbers: List<Int>): Flow<List<Record>> {
        return dao.getRecordsByRecordNumbers(recordNumbers)
    }

}