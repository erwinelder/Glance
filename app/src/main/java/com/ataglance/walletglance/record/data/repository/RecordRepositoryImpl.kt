package com.ataglance.walletglance.record.data.repository

import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.utils.synchroniseDataFromRemote
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.data.local.source.RecordLocalDataSource
import com.ataglance.walletglance.record.data.mapper.toLocalEntity
import com.ataglance.walletglance.record.data.mapper.toRemoteEntity
import com.ataglance.walletglance.record.data.remote.model.RecordRemoteEntity
import com.ataglance.walletglance.record.data.remote.source.RecordRemoteDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class RecordRepositoryImpl(
    private val localSource: RecordLocalDataSource,
    private val remoteSource: RecordRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : RecordRepository {

    private suspend fun synchroniseRecords() {
        val userId = syncHelper.getUserIdForSync(TableName.Record) ?: return

        synchroniseDataFromRemote(
            localUpdateTimeGetter = localSource::getUpdateTime,
            remoteUpdateTimeGetter = { remoteSource.getUpdateTime(userId = userId) },
            remoteDataGetter = { timestamp ->
                remoteSource.getRecordsAfterTimestamp(timestamp = timestamp, userId = userId)
            },
            remoteDataToLocalDataMapper = RecordRemoteEntity::toLocalEntity,
            localSynchroniser = localSource::synchroniseRecords
        )
    }

    override suspend fun upsertRecords(records: List<RecordEntity>) {
        val timestamp = getCurrentTimestamp()

        val upsertedRecords = localSource.upsertRecords(records = records, timestamp = timestamp)
        syncHelper.tryToSyncToRemote(TableName.Record) { userId ->
            remoteSource.upsertRecords(
                records = upsertedRecords.map {
                    it.toRemoteEntity(updateTime = timestamp, deleted = false)
                },
                timestamp = timestamp,
                userId = userId
            )
        }
    }

    override suspend fun deleteRecords(records: List<RecordEntity>) {
        val timestamp = getCurrentTimestamp()

        localSource.deleteRecords(records = records, timestamp = timestamp)
        syncHelper.tryToSyncToRemote(TableName.Record) { userId ->
            remoteSource.upsertRecords(
                records = records.map {
                    it.toRemoteEntity(updateTime = timestamp, deleted = true)
                },
                timestamp = timestamp,
                userId = userId
            )
        }
    }

    override suspend fun deleteAndUpsertRecords(
        toDelete: List<RecordEntity>,
        toUpsert: List<RecordEntity>
    ) {
        val timestamp = getCurrentTimestamp()
        val recordsToSync = EntitiesToSync(toDelete = toDelete, toUpsert = toUpsert)

        val upsertedRecords = localSource.synchroniseRecords(
            recordsToSync = recordsToSync, timestamp = timestamp
        )
        syncHelper.tryToSyncToRemote(TableName.Record) { userId ->
            remoteSource.synchroniseRecords(
                recordsToSync = recordsToSync.copy(toUpsert = upsertedRecords)
                    .map { deleted -> toRemoteEntity(updateTime = timestamp, deleted = deleted) },
                timestamp = timestamp,
                userId = userId
            )
        }
    }

    override suspend fun deleteAllRecordsLocally() {
        val timestamp = getCurrentTimestamp()
        localSource.deleteAllRecords(timestamp = timestamp)
    }

    override suspend fun deleteRecordsByAccounts(accountIds: List<Int>) {
        val timestamp = getCurrentTimestamp()

        localSource.deleteRecordsByAccounts(accountIds = accountIds, timestamp = timestamp)
        syncHelper.tryToSyncToRemote(TableName.Record) { userId ->
            remoteSource.deleteRecordsByAccounts(
                accountIds = accountIds, timestamp = timestamp, userId = userId
            )
        }
    }

    override fun getLastRecordNumFlow(): Flow<Int?> = flow {
        coroutineScope {
            launch { synchroniseRecords() }
            localSource.getLastRecordNum().collect(::emit)
        }
    }

    override suspend fun getLastRecordNum(): Int? {
        synchroniseRecords()
        return localSource.getLastRecordNum().firstOrNull()
    }

    override suspend fun getLastRecordsByTypeAndAccount(
        type: Char,
        accountId: Int
    ): List<RecordEntity> {
        synchroniseRecords()
        return localSource.getLastRecordsByTypeAndAccount(type = type, accountId = accountId)
    }

    override suspend fun getRecordsByRecordNum(recordNum: Int): List<RecordEntity> {
        synchroniseRecords()
        return localSource.getRecordsByRecordNum(recordNum = recordNum)
    }

    override fun getRecordsInDateRangeFlow(range: LongDateRange): Flow<List<RecordEntity>> = flow {
        coroutineScope {
            launch { synchroniseRecords() }
            localSource.getRecordsInDateRange(range = range).collect(::emit)
        }
    }

    override suspend fun getRecordsInDateRange(range: LongDateRange): List<RecordEntity> {
        synchroniseRecords()
        return localSource.getRecordsInDateRange(range = range).firstOrNull().orEmpty()
    }

    override suspend fun getTotalAmountByCategoryAndAccountsInRange(
        categoryId: Int,
        accountsIds: List<Int>,
        dateRange: LongDateRange
    ): Double {
        synchroniseRecords()
        return localSource.getTotalAmountByCategoryAndAccountsInRange(
            categoryId = categoryId,
            linkedAccountsIds = accountsIds,
            longDateRange = dateRange
        )
    }

}