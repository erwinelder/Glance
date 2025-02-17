package com.ataglance.walletglance.record.data.repository

import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.utils.synchroniseData
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.data.local.source.RecordLocalDataSource
import com.ataglance.walletglance.record.data.mapper.toLocalEntity
import com.ataglance.walletglance.record.data.mapper.toRemoteEntity
import com.ataglance.walletglance.record.data.remote.model.RecordRemoteEntity
import com.ataglance.walletglance.record.data.remote.source.RecordRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecordRepositoryImpl(
    private val localSource: RecordLocalDataSource,
    private val remoteSource: RecordRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : RecordRepository {

    private suspend fun synchroniseRecords() {
        val userId = syncHelper.getUserIdForSync(TableName.Record) ?: return

        synchroniseData(
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

        localSource.upsertRecords(records = records, timestamp = timestamp)
        syncHelper.tryToSyncToRemote(TableName.Record) { userId ->
            remoteSource.upsertRecords(
                records = records.map {
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

        localSource.synchroniseRecords(recordsToSync = recordsToSync, timestamp = timestamp)
        syncHelper.tryToSyncToRemote(TableName.Record) { userId ->
            remoteSource.synchroniseRecords(
                recordsToSync = recordsToSync.map { deleted ->
                    toRemoteEntity(updateTime = timestamp, deleted = deleted)
                },
                timestamp = timestamp,
                userId = userId
            )
        }
    }

    override suspend fun deleteAllRecordsLocally() {
        val timestamp = getCurrentTimestamp()
        localSource.deleteAllRecords(timestamp = timestamp)
    }

    override suspend fun convertRecordsToTransfers(noteValues: List<String>) {
        val timestamp = getCurrentTimestamp()
        localSource.convertTransfersToRecords(noteValues, timestamp)
        syncHelper.tryToSyncToRemote(TableName.Record) { userId ->
            remoteSource.convertTransfersToRecords(
                noteValues = noteValues, timestamp = timestamp, userId = userId
            )
        }
    }

    override fun getLastRecordNum(): Flow<Int?> = flow {
        synchroniseRecords()
        localSource.getLastRecordNum().collect(::emit)
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

    override fun getRecordsInDateRange(range: LongDateRange): Flow<List<RecordEntity>> = flow {
        synchroniseRecords()
        localSource.getRecordsInDateRange(range = range).collect(::emit)
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