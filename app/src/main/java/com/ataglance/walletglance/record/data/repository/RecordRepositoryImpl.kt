package com.ataglance.walletglance.record.data.repository

import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.core.utils.asList
import com.ataglance.walletglance.record.data.local.model.RecordEntityWithItems
import com.ataglance.walletglance.record.data.local.source.RecordLocalDataSource
import com.ataglance.walletglance.record.data.mapper.toCommandDtoWithItems
import com.ataglance.walletglance.record.data.mapper.toDataModelWithItems
import com.ataglance.walletglance.record.data.mapper.toEntityWithItems
import com.ataglance.walletglance.record.data.model.RecordDataModelWithItems
import com.ataglance.walletglance.record.data.remote.model.RecordQueryDtoWithItems
import com.ataglance.walletglance.record.data.remote.source.RecordRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class RecordRepositoryImpl(
    private val localSource: RecordLocalDataSource,
    private val remoteSource: RecordRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : RecordRepository {

    private suspend fun synchronizeRecords() {
        syncHelper.synchronizeData(
            tableName = TableName.Record,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localDataGetter = { timestamp ->
                localSource.getRecordsWithItemsAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, userId ->
                remoteSource.getRecordsWithItemsAfterTimestamp(
                    timestamp = timestamp, userId = userId
                )
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveRecordsWithItems(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, userId ->
                remoteSource.synchronizeRecordsWithItems(
                    recordsWithItems = data, timestamp = timestamp, userId = userId
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = RecordEntityWithItems::toCommandDtoWithItems,
            queryDtoToEntityMapper = RecordQueryDtoWithItems::toEntityWithItems
        )
    }

    override suspend fun upsertRecordWithItems(recordWithItems: RecordDataModelWithItems) {
        upsertRecordsWithItems(recordsWithItems = recordWithItems.asList())
    }

    override suspend fun upsertRecordsWithItems(recordsWithItems: List<RecordDataModelWithItems>) {
        syncHelper.upsertData(
            data = recordsWithItems,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveRecordsWithItems(
                    recordsWithItems = entities, timestamp = timestamp
                )
            },
            remoteSoftCommand = { dtos, timestamp, userId ->
                remoteSource.synchronizeRecordsWithItems(
                    recordsWithItems = dtos, timestamp = timestamp, userId = userId
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getRecordsWithItemsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, userId, localTimestamp ->
                remoteSource.synchronizeRecordsWithItemsAndGetAfterTimestamp(
                    recordsWithItems = dtos,
                    timestamp = timestamp,
                    userId = userId,
                    localTimestamp = localTimestamp
                )
            },
            dataModelToEntityMapper = RecordDataModelWithItems::toEntityWithItems,
            dataModelToCommandDtoMapper = RecordDataModelWithItems::toCommandDtoWithItems,
            entityToCommandDtoMapper = RecordEntityWithItems::toCommandDtoWithItems,
            queryDtoToEntityMapper = RecordQueryDtoWithItems::toEntityWithItems
        )
    }

    override suspend fun deleteRecordWithItems(recordWithItems: RecordDataModelWithItems) {
        syncHelper.deleteData(
            data = recordWithItems.asList(),
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveRecordsWithItems(
                    recordsWithItems = entities, timestamp = timestamp
                )
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveRecordsWithItems(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities, timestamp ->
                localSource.deleteRecordsWithItems(
                    recordsWithItems = entities, timestamp = timestamp
                )
            },
            remoteSoftCommand = { dtos, timestamp, userId ->
                remoteSource.synchronizeRecordsWithItems(
                    recordsWithItems = dtos, timestamp = timestamp, userId = userId
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getRecordsWithItemsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, userId, localTimestamp ->
                remoteSource.synchronizeRecordsWithItemsAndGetAfterTimestamp(
                    recordsWithItems = dtos,
                    timestamp = timestamp,
                    userId = userId,
                    localTimestamp = localTimestamp
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = RecordDataModelWithItems::toEntityWithItems,
            dataModelToCommandDtoMapper = RecordDataModelWithItems::toCommandDtoWithItems,
            entityToCommandDtoMapper = RecordEntityWithItems::toCommandDtoWithItems,
            queryDtoToEntityMapper = RecordQueryDtoWithItems::toEntityWithItems
        )
    }

    override suspend fun deleteAndUpsertRecordWithItems(
        recordWithItemsToDelete: RecordDataModelWithItems,
        recordWithItemsToUpsert: RecordDataModelWithItems
    ) {
        syncHelper.deleteAndUpsertData(
            toDelete = recordWithItemsToDelete.asList(),
            toUpsert = recordWithItemsToUpsert.asList(),
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveRecordsWithItems(
                    recordsWithItems = entities, timestamp = timestamp
                )
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveRecordsWithItems(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities ->
                localSource.deleteRecordsWithItems(recordsWithItems = entities)
            },
            remoteSoftCommand = { dtos, timestamp, userId ->
                remoteSource.synchronizeRecordsWithItems(
                    recordsWithItems = dtos, timestamp = timestamp, userId = userId
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getRecordsWithItemsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, userId, localTimestamp ->
                remoteSource.synchronizeRecordsWithItemsAndGetAfterTimestamp(
                    recordsWithItems = dtos,
                    timestamp = timestamp,
                    userId = userId,
                    localTimestamp = localTimestamp
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = RecordDataModelWithItems::toEntityWithItems,
            dataModelToCommandDtoMapper = RecordDataModelWithItems::toCommandDtoWithItems,
            entityToCommandDtoMapper = RecordEntityWithItems::toCommandDtoWithItems,
            queryDtoToEntityMapper = RecordQueryDtoWithItems::toEntityWithItems
        )
    }

    override suspend fun getRecordWithItems(id: Long): RecordDataModelWithItems? {
        synchronizeRecords()
        return localSource.getRecordWithItems(id = id)?.toDataModelWithItems()
    }

    override suspend fun getLastRecordWithItemsByTypeAndAccount(
        type: Char,
        accountId: Int
    ): RecordDataModelWithItems? {
        synchronizeRecords()
        return localSource
            .getLastRecordWithItemsByTypeAndAccount(type = type, accountId = accountId)
            ?.toDataModelWithItems()
    }

    override fun getRecordsWithItemsInDateRangeAsFlow(
        from: Long,
        to: Long
    ): Flow<List<RecordDataModelWithItems>> {
        return localSource.getRecordsWithItemsInDateRangeAsFlow(from = from, to = to)
            .onStart { synchronizeRecords() }
            .map { recordsWithItems ->
                recordsWithItems.map { it.toDataModelWithItems() }
            }
    }

    override suspend fun getRecordsWithItemsInDateRange(
        from: Long,
        to: Long
    ): List<RecordDataModelWithItems> {
        synchronizeRecords()
        return localSource.getRecordsWithItemsInDateRange(from = from, to = to)
            .map { it.toDataModelWithItems() }
    }

    override suspend fun getTotalExpensesInDateRangeByAccountsAndCategory(
        dateRange: TimestampRange,
        accountIds: List<Int>,
        categoryId: Int
    ): Double {
        synchronizeRecords()
        return localSource.getTotalExpensesInDateRangeByAccountsAndCategory(
            from = dateRange.from,
            to = dateRange.to,
            accountIds = accountIds,
            categoryId = categoryId
        )
    }

}