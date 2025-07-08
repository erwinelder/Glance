package com.ataglance.walletglance.record.data.local.source

import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.record.data.local.dao.RecordLocalDao
import com.ataglance.walletglance.record.data.local.model.RecordEntityWithItems
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

    override suspend fun deleteUpdateTime() {
        updateTimeDao.deleteUpdateTime(tableName = TableName.Record.name)
    }

    override suspend fun saveRecordsWithItems(
        recordsWithItems: List<RecordEntityWithItems>,
        timestamp: Long
    ): List<RecordEntityWithItems> {
        return recordDao.saveRecordsWithItems(recordsWithItems = recordsWithItems).also {
            saveUpdateTime(timestamp = timestamp)
        }
    }

    override suspend fun deleteRecordsWithItems(
        recordsWithItems: List<RecordEntityWithItems>,
        timestamp: Long?
    ) {
        recordDao.deleteRecordsWithItems(recordsWithItems = recordsWithItems)
        timestamp?.let { saveUpdateTime(timestamp = it) }
    }

    override suspend fun deleteAndSaveRecordsWithItems(
        toDelete: List<RecordEntityWithItems>,
        toUpsert: List<RecordEntityWithItems>,
        timestamp: Long
    ): List<RecordEntityWithItems> {
        return recordDao.deleteAndSaveRecordsWithItems(toDelete = toDelete, toUpsert = toUpsert)
            .also { saveUpdateTime(timestamp = timestamp) }
    }

    override suspend fun getRecordsWithItemsAfterTimestamp(
        timestamp: Long
    ): List<RecordEntityWithItems> {
        return recordDao.getRecordsWithItemsAfterTimestamp(timestamp = timestamp)
    }

    override suspend fun getRecordWithItems(id: Long): RecordEntityWithItems? {
        return recordDao.getRecordWithItems(id = id)
    }

    override suspend fun getLastRecordWithItemsByTypeAndAccount(
        type: Char,
        accountId: Int
    ): RecordEntityWithItems? {
        return recordDao.getLastRecordWithItemsByTypeAndAccount(type = type, accountId = accountId)
    }

    override fun getRecordsWithItemsInDateRangeAsFlow(
        from: Long,
        to: Long
    ): Flow<List<RecordEntityWithItems>> {
        return recordDao.getRecordsWithItemsInDateRangeAsFlow(from = from, to = to)
    }

    override suspend fun getRecordsWithItemsInDateRange(
        from: Long,
        to: Long
    ): List<RecordEntityWithItems> {
        return recordDao.getRecordsWithItemsInDateRange(from = from, to = to)
    }

    override suspend fun getTotalExpensesInDateRangeByAccountsAndCategory(
        from: Long,
        to: Long,
        accountIds: List<Int>,
        categoryId: Int
    ): Double {
        return recordDao.getTotalExpensesInDateRangeByAccountsAndCategory(
            from = from,
            to = to,
            accountIds = accountIds,
            categoryId = categoryId
        )
    }

}

fun getRecordLocalDataSource(appDatabase: AppDatabase): RecordLocalDataSource {
    return RecordLocalDataSourceImpl(
        recordDao = appDatabase.recordDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}
