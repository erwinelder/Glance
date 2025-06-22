package com.ataglance.walletglance.record.data.local.source

import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.domain.date.TimestampRange
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

    override suspend fun upsertRecords(records: List<RecordEntity>, timestamp: Long): List<RecordEntity> {
        return recordDao.upsertRecords(records = records).also {
            saveUpdateTime(timestamp = timestamp)
        }
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
    ): List<RecordEntity> {
        return recordDao.deleteAndUpsertRecords(
            toDelete = recordsToSync.toDelete,
            toUpsert = recordsToSync.toUpsert
        ).also {
            saveUpdateTime(timestamp = timestamp)
        }
    }

    override suspend fun deleteRecordsByAccounts(accountIds: List<Int>, timestamp: Long) {
        recordDao.deleteRecordsByAccounts(accountIds = accountIds)
        recordDao.convertTransfersToRecords(noteValues = accountIds.map { it.toString() })
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
        range: TimestampRange
    ): Flow<List<RecordEntity>> {
        return recordDao.getRecordsInDateRange(
            startPastDate = range.from, endFutureDate = range.to
        )
    }

    override suspend fun getTotalAmountByCategoryAndAccountsInRange(
        categoryId: Int,
        linkedAccountsIds: List<Int>,
        dateRange: TimestampRange
    ): Double {
        return recordDao.getTotalAmountByCategoryAndAccountsInRange(
            categoryId = categoryId,
            linkedAccountsIds = linkedAccountsIds,
            from = dateRange.from,
            to = dateRange.to
        ) ?: 0.0
    }

}

fun getRecordLocalDataSource(appDatabase: AppDatabase): RecordLocalDataSource {
    return RecordLocalDataSourceImpl(
        recordDao = appDatabase.recordDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}