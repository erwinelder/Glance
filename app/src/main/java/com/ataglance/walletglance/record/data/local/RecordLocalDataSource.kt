package com.ataglance.walletglance.record.data.local

import androidx.room.Transaction
import com.ataglance.walletglance.core.data.local.source.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.model.LongDateRange
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.record.data.model.RecordEntity
import kotlinx.coroutines.flow.Flow

class RecordLocalDataSource(
    private val recordDao: RecordDao,
    updateTimeDao: LocalUpdateTimeDao
) : BaseLocalDataSource<RecordEntity>(
    dao = recordDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.Record
) {

    @Transaction
    suspend fun deleteAllRecords(timestamp: Long) {
        recordDao.deleteAllRecords()
        updateLastModifiedTime(timestamp)
    }

    suspend fun convertTransfersToRecords(noteValues: List<String>, timestamp: Long) {
        recordDao.convertTransfersToRecords(noteValues)
        updateLastModifiedTime(timestamp)
    }

    fun getLastRecordOrderNum(): Flow<Int?> = recordDao.getLastRecordOrderNum()

    fun getRecordsInDateRange(longDateRange: LongDateRange): Flow<List<RecordEntity>> =
        recordDao.getRecordsInDateRange(longDateRange.from, longDateRange.to)

    fun getTotalAmountForBudgetInDateRange(
        linkedAccountsIds: List<Int>,
        categoryId: Int,
        longDateRange: LongDateRange
    ): Flow<Double> = recordDao.getTotalAmountForBudgetInDateRange(
        linkedAccountsIds = linkedAccountsIds,
        categoryId = categoryId,
        from = longDateRange.from,
        to = longDateRange.to
    )

}