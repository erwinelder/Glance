package com.ataglance.walletglance.record.data.repository

import com.ataglance.walletglance.auth.data.model.UserContext
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.TotalAmountByRange
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.LongDateRange
import com.ataglance.walletglance.core.data.utils.synchroniseData
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import com.ataglance.walletglance.core.utils.getTodayLongDateRange
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
    private val userContext: UserContext
) : RecordRepository {

    private suspend fun synchroniseRecords() {
        val userId = userContext.getUserId() ?: return

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
        userContext.getUserId()?.let { userId ->
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
        userContext.getUserId()?.let { userId ->
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
        userContext.getUserId()?.let { userId ->
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
        userContext.getUserId()?.let { userId ->
            remoteSource.convertTransfersToRecords(
                noteValues = noteValues, timestamp = timestamp, userId = userId
            )
        }
    }

    override fun getLastRecordNum(): Flow<Int?> = flow {
        synchroniseRecords()
        localSource.getLastRecordNum().collect(::emit)
    }

    override fun getRecordsForToday(): Flow<List<RecordEntity>> = flow {
        synchroniseRecords()
        localSource.getRecordsInDateRange(range = getTodayLongDateRange()).collect(::emit)
    }

    override suspend fun getRecordsByRecordNum(recordNum: Int): List<RecordEntity> {
        synchroniseRecords()
        return localSource.getRecordsByRecordNum(recordNum = recordNum)
    }

    override fun getRecordsInDateRange(range: LongDateRange): Flow<List<RecordEntity>> = flow {
        synchroniseRecords()
        localSource.getRecordsInDateRange(range = range).collect(::emit)
    }

    override fun getTotalAmountForBudgetInDateRanges(
        budget: Budget,
        dateRangeList: List<LongDateRange>
    ): Flow<List<TotalAmountByRange>> = flow {
        val initialList = dateRangeList.map { TotalAmountByRange(it, 0.0) }
        emit(initialList)

        budget.category ?: return@flow

        val budgetsAmountsByRanges = initialList.toMutableList()

        dateRangeList.forEachIndexed { index, dateRange ->
            val totalAmount = localSource.getTotalAmountForBudgetInDateRange(
                linkedAccountsIds = budget.linkedAccountsIds,
                categoryId = budget.category.id,
                longDateRange = dateRange
            )

            budgetsAmountsByRanges[index] = TotalAmountByRange(
                dateRange = dateRange, totalAmount = totalAmount ?: 0.0
            )
            emit(budgetsAmountsByRanges)
        }
    }

}