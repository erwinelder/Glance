package com.ataglance.walletglance.transfer.data.repository

import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.utils.asList
import com.ataglance.walletglance.transfer.data.local.model.TransferEntity
import com.ataglance.walletglance.transfer.data.local.source.TransferLocalDataSource
import com.ataglance.walletglance.transfer.data.mapper.toCommandDto
import com.ataglance.walletglance.transfer.data.mapper.toDataModel
import com.ataglance.walletglance.transfer.data.mapper.toEntity
import com.ataglance.walletglance.transfer.data.model.TransferDataModel
import com.ataglance.walletglance.transfer.data.remote.model.TransferQueryDto
import com.ataglance.walletglance.transfer.data.remote.source.TransferRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class TransferRepositoryImpl(
    private val localSource: TransferLocalDataSource,
    private val remoteSource: TransferRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : TransferRepository {

    private suspend fun synchronizeTransfers() {
        syncHelper.synchronizeData(
            tableName = TableName.Transfer,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localDataGetter = { timestamp ->
                localSource.getTransfersAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, userId ->
                remoteSource.getTransfersAfterTimestamp(timestamp = timestamp, userId = userId)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveTransfers(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, userId ->
                remoteSource.synchronizeTransfers(
                    transfers = data, timestamp = timestamp, userId = userId
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = TransferEntity::toCommandDto,
            queryDtoToEntityMapper = TransferQueryDto::toEntity
        )
    }

    override suspend fun upsertTransfer(transfer: TransferDataModel) {
        syncHelper.upsertData(
            data = transfer.asList(),
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveTransfers(
                    transfers = entities, timestamp = timestamp
                )
            },
            remoteSoftCommand = { dtos, timestamp, userId ->
                remoteSource.synchronizeTransfers(
                    transfers = dtos, timestamp = timestamp, userId = userId
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getTransfersAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, userId, localTimestamp ->
                remoteSource.synchronizeTransfersAndGetAfterTimestamp(
                    transfers = dtos,
                    timestamp = timestamp,
                    userId = userId,
                    localTimestamp = localTimestamp
                )
            },
            dataModelToEntityMapper = TransferDataModel::toEntity,
            dataModelToCommandDtoMapper = TransferDataModel::toCommandDto,
            entityToCommandDtoMapper = TransferEntity::toCommandDto,
            queryDtoToEntityMapper = TransferQueryDto::toEntity
        )
    }

    override suspend fun deleteTransfer(transfer: TransferDataModel) {
        syncHelper.deleteData(
            data = transfer.asList(),
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveTransfers(
                    transfers = entities, timestamp = timestamp
                )
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveTransfers(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities, timestamp ->
                localSource.deleteTransfers(transfers = entities, timestamp = timestamp)
            },
            remoteSoftCommand = { dtos, timestamp, userId ->
                remoteSource.synchronizeTransfers(
                    transfers = dtos, timestamp = timestamp, userId = userId
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getTransfersAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, userId, localTimestamp ->
                remoteSource.synchronizeTransfersAndGetAfterTimestamp(
                    transfers = dtos,
                    timestamp = timestamp,
                    userId = userId,
                    localTimestamp = localTimestamp
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = TransferDataModel::toEntity,
            dataModelToCommandDtoMapper = TransferDataModel::toCommandDto,
            entityToCommandDtoMapper = TransferEntity::toCommandDto,
            queryDtoToEntityMapper = TransferQueryDto::toEntity
        )
    }

    override suspend fun getTransfer(id: Long): TransferDataModel? {
        synchronizeTransfers()
        return localSource.getTransfer(id = id)?.toDataModel()
    }

    override fun getTransfersInDateRangeAsFlow(
        from: Long,
        to: Long
    ): Flow<List<TransferDataModel>> {
        return localSource.getTransfersInDateRangeAsFlow(from = from, to = to)
            .onStart { synchronizeTransfers() }
            .map { transfers ->
                transfers.map { it.toDataModel() }
            }
    }

    override suspend fun getTransfersInDateRange(
        from: Long,
        to: Long
    ): List<TransferDataModel> {
        synchronizeTransfers()
        return localSource.getTransfersInDateRange(from = from, to = to).map { it.toDataModel() }
    }

    override suspend fun getTransfersByAccounts(ids: List<Int>): List<TransferDataModel> {
        synchronizeTransfers()
        return localSource.getTransfersByAccounts(accountIds = ids).map { it.toDataModel() }
    }

    override suspend fun getTotalExpensesInDateRangeByAccounts(
        from: Long,
        to: Long,
        accountIds: List<Int>
    ): Double {
        synchronizeTransfers()
        return localSource.getTotalExpensesInDateRangeByAccounts(
            from = from, to = to, accountIds = accountIds
        )
    }

}