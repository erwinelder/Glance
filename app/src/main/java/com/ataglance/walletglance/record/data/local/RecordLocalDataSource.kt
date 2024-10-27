package com.ataglance.walletglance.record.data.local

import androidx.room.Transaction
import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.TableUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.record.data.model.RecordEntity
import kotlinx.coroutines.flow.Flow

class RecordLocalDataSource(
    private val recordDao: RecordDao,
    updateTimeDao: TableUpdateTimeDao
) : BaseLocalDataSource<RecordEntity>(
    dao = recordDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.Record
) {

    @Transaction
    suspend fun deleteRecordsByRecordNumbers(recordNumbers: List<Int>, timestamp: Long) {
        recordDao.deleteRecordsByRecordNumbers(recordNumbers)
        updateLastModifiedTime(timestamp)
    }

    @Transaction
    suspend fun deleteAllRecords(timestamp: Long) {
        recordDao.deleteAllRecords()
        updateLastModifiedTime(timestamp)
    }

    fun getLastRecordOrderNum(): Flow<Int?> = recordDao.getLastRecordOrderNum()

    fun getRecordsByRecordNumbers(recordNumbers: List<Int>): Flow<List<RecordEntity>> =
        recordDao.getRecordsByRecordNumbers(recordNumbers)

    fun getRecordsInDateRange(startPastDate: Long, endFutureDate: Long): Flow<List<RecordEntity>> =
        recordDao.getRecordsInDateRange(startPastDate, endFutureDate)

    fun getTotalAmountForBudgetInDateRange(
        linkedAccountsIds: List<Int>,
        categoryId: Int,
        from: Long,
        to: Long
    ): Flow<Double> = recordDao.getTotalAmountForBudgetInDateRange(
        linkedAccountsIds = linkedAccountsIds,
        categoryId = categoryId,
        from = from,
        to = to
    )

    fun getTransfersByAccountId(accountId: Int): Flow<List<RecordEntity>> =
        recordDao.getTransfersByAccountId(accountId)

    suspend fun convertTransfersToRecords(noteValues: List<String>, timestamp: Long) {
        recordDao.convertTransfersToRecords(noteValues)
        updateLastModifiedTime(timestamp)
    }

}