package com.ataglance.walletglance.record.data.local.source

import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.LongDateRange
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.record.data.local.dao.RecordLocalDao
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import kotlinx.coroutines.flow.Flow

class RecordLocalDataSourceImpl(
    private val recordDao: RecordLocalDao,
    private val updateTimeDao: LocalUpdateTimeDao
) : RecordLocalDataSource {

    override suspend fun getUpdateTime(): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.Record.name)
    }

    override suspend fun saveUpdateTime(timestamp: Long) {
        updateTimeDao.saveUpdateTime(tableName = TableName.Record.name, timestamp = timestamp)
    }

    override suspend fun upsertRecords(records: List<RecordEntity>, timestamp: Long) {
        recordDao.upsertRecords(records = records)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun deleteRecords(records: List<RecordEntity>, timestamp: Long) {
        recordDao.deleteRecords(records = records)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun deleteAllRecords(timestamp: Long) {
        recordDao.deleteAllRecords()
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun synchroniseRecords(
        recordsToSync: EntitiesToSync<RecordEntity>,
        timestamp: Long
    ) {
        recordDao.deleteAndUpsertRecords(
            toDelete = recordsToSync.toDelete,
            toUpsert = recordsToSync.toUpsert
        )
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun convertTransfersToRecords(noteValues: List<String>, timestamp: Long) {
        recordDao.convertTransfersToRecords(noteValues = noteValues)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun getLastRecordNum(): Flow<Int?> {
        return recordDao.getLastRecordOrderNum()
    }

    override suspend fun getLastRecordsByTypeAndAccount(
        type: Char,
        accountId: Int
    ): List<RecordEntity> {
        return recordDao.getLastRecordsByTypeAndAccount(type = type, accountId = accountId)
            .takeIf { it.isNotEmpty() }
            ?: recordDao.getLastRecordsByType(type = type)
    }

    override suspend fun getRecordsByRecordNum(recordNum: Int): List<RecordEntity> {
        return recordDao.getRecordsByRecordNum(recordNum = recordNum)
    }

    override suspend fun getRecordsInDateRange(
        range: LongDateRange
    ): Flow<List<RecordEntity>> {
        return recordDao.getRecordsInDateRange(
            startPastDate = range.from, endFutureDate = range.to
        )
    }

    override suspend fun getTotalAmountForBudgetInDateRange(
        linkedAccountsIds: List<Int>,
        categoryId: Int,
        longDateRange: LongDateRange
    ): Double? {
        return recordDao.getTotalAmountForBudgetInDateRange(
            linkedAccountsIds = linkedAccountsIds,
            categoryId = categoryId,
            from = longDateRange.from,
            to = longDateRange.to
        )
    }

}

fun getRecordLocalDataSource(appDatabase: AppDatabase): RecordLocalDataSource {
    return RecordLocalDataSourceImpl(
        recordDao = appDatabase.recordDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}