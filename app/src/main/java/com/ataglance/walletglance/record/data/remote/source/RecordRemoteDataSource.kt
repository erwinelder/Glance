package com.ataglance.walletglance.record.data.remote.source

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.record.data.remote.model.RecordRemoteEntity

interface RecordRemoteDataSource {

    suspend fun getUpdateTime(userId: String): Long?

    suspend fun saveUpdateTime(timestamp: Long, userId: String)

    suspend fun upsertRecords(records: List<RecordRemoteEntity>, timestamp: Long, userId: String)

    suspend fun synchroniseRecords(
        recordsToSync: EntitiesToSync<RecordRemoteEntity>,
        timestamp: Long,
        userId: String
    )

    suspend fun deleteRecordsByAccounts(accountIds: List<Int>, timestamp: Long, userId: String)

    suspend fun getRecordsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<RecordRemoteEntity>

}