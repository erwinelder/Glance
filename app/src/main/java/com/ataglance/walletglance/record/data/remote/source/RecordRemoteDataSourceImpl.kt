package com.ataglance.walletglance.record.data.remote.source

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.dao.RemoteUpdateTimeDao
import com.ataglance.walletglance.record.data.remote.dao.RecordRemoteDao
import com.ataglance.walletglance.record.data.remote.model.RecordRemoteEntity

class RecordRemoteDataSourceImpl(
    private val recordDao: RecordRemoteDao,
    private val updateTimeDao: RemoteUpdateTimeDao
) : RecordRemoteDataSource {

    override suspend fun getUpdateTime(userId: String): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.Record.name, userId = userId)
    }

    override suspend fun saveUpdateTime(timestamp: Long, userId: String) {
        updateTimeDao.saveUpdateTime(
            tableName = TableName.Record.name, timestamp = timestamp, userId = userId
        )
    }

    override suspend fun upsertRecords(
        records: List<RecordRemoteEntity>,
        timestamp: Long,
        userId: String
    ) {
        recordDao.upsertRecords(records = records, userId = userId)
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun synchroniseRecords(
        recordsToSync: EntitiesToSync<RecordRemoteEntity>,
        timestamp: Long,
        userId: String
    ) {
        recordDao.synchroniseRecords(recordsToSync = recordsToSync, userId = userId)
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun convertTransfersToRecords(
        noteValues: List<String>,
        timestamp: Long,
        userId: String
    ) {
        recordDao.convertTransfersToRecords(
            noteValues = noteValues, timestamp = timestamp, userId = userId
        )
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun getRecordsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<RecordRemoteEntity> {
        return recordDao.getRecordsAfterTimestamp(timestamp = timestamp, userId = userId)
    }

}