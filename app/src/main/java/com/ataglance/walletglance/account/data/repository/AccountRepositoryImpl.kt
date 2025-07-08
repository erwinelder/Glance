package com.ataglance.walletglance.account.data.repository

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.data.local.source.AccountLocalDataSource
import com.ataglance.walletglance.account.data.mapper.toCommandDto
import com.ataglance.walletglance.account.data.mapper.toDataModel
import com.ataglance.walletglance.account.data.mapper.toEntity
import com.ataglance.walletglance.account.data.model.AccountDataModel
import com.ataglance.walletglance.account.data.remote.model.AccountQueryDto
import com.ataglance.walletglance.account.data.remote.source.AccountRemoteDataSource
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class AccountRepositoryImpl(
    private val localSource: AccountLocalDataSource,
    private val remoteSource: AccountRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : AccountRepository {

    private suspend fun synchronizeAccounts() {
        syncHelper.synchronizeData(
            tableName = TableName.Account,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localDataGetter = { timestamp ->
                localSource.getAccountsAfterTimestamp(timestamp = timestamp)
            },
            remoteDataGetter = { timestamp, userId ->
                remoteSource.getAccountsAfterTimestamp(timestamp = timestamp, userId = userId)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveAccounts(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            remoteSynchronizer = { data, timestamp, userId ->
                remoteSource.synchronizeAccounts(
                    accounts = data, timestamp = timestamp, userId = userId
                )
            },
            entityDeletedPredicate = { it.deleted },
            entityToCommandDtoMapper = AccountEntity::toCommandDto,
            queryDtoToEntityMapper = AccountQueryDto::toEntity
        )
    }

    override suspend fun upsertAccounts(accounts: List<AccountDataModel>) {
        syncHelper.upsertData(
            data = accounts,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveAccounts(accounts = entities, timestamp = timestamp)
            },
            remoteSoftCommand = { dtos, timestamp, userId ->
                remoteSource.synchronizeAccounts(
                    accounts = dtos, timestamp = timestamp, userId = userId
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getAccountsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, userId, localTimestamp ->
                remoteSource.synchronizeAccountsAndGetAfterTimestamp(
                    accounts = dtos,
                    timestamp = timestamp,
                    userId = userId,
                    localTimestamp = localTimestamp
                )
            },
            dataModelToEntityMapper = AccountDataModel::toEntity,
            dataModelToCommandDtoMapper = AccountDataModel::toCommandDto,
            entityToCommandDtoMapper = AccountEntity::toCommandDto,
            queryDtoToEntityMapper = AccountQueryDto::toEntity
        )
    }

    override suspend fun deleteAndUpsertAccounts(
        toDelete: List<AccountDataModel>,
        toUpsert: List<AccountDataModel>
    ) {
        syncHelper.deleteAndUpsertData(
            toDelete = toDelete,
            toUpsert = toUpsert,
            localTimestampGetter = { localSource.getUpdateTime() },
            remoteTimestampGetter = { userId -> remoteSource.getUpdateTime(userId = userId) },
            localSoftCommand = { entities, timestamp ->
                localSource.saveAccounts(accounts = entities, timestamp = timestamp)
            },
            localHardCommand = { entitiesToDelete, entitiesToUpsert, timestamp ->
                localSource.deleteAndSaveAccounts(
                    toDelete = entitiesToDelete, toUpsert = entitiesToUpsert, timestamp = timestamp
                )
            },
            localDeleteCommand = { entities ->
                localSource.deleteAccounts(accounts = entities)
            },
            remoteSoftCommand = { dtos, timestamp, userId ->
                remoteSource.synchronizeAccounts(
                    accounts = dtos, timestamp = timestamp, userId = userId
                )
            },
            localDataAfterTimestampGetter = { timestamp ->
                localSource.getAccountsAfterTimestamp(timestamp = timestamp)
            },
            remoteSoftCommandAndDataAfterTimestampGetter = { dtos, timestamp, userId, localTimestamp ->
                remoteSource.synchronizeAccountsAndGetAfterTimestamp(
                    accounts = dtos,
                    timestamp = timestamp,
                    userId = userId,
                    localTimestamp = localTimestamp
                )
            },
            entityDeletedPredicate = { it.deleted },
            dataModelToEntityMapper = AccountDataModel::toEntity,
            dataModelToCommandDtoMapper = AccountDataModel::toCommandDto,
            entityToCommandDtoMapper = AccountEntity::toCommandDto,
            queryDtoToEntityMapper = AccountQueryDto::toEntity
        )
    }

    override suspend fun deleteAllAccountsLocally() {
        localSource.deleteAllAccounts()
    }

    override suspend fun getAccount(id: Int): AccountDataModel? {
        synchronizeAccounts()
        return localSource.getAccount(id = id)?.toDataModel()
    }

    override suspend fun getAccounts(ids: List<Int>): List<AccountDataModel> {
        synchronizeAccounts()
        return localSource
            .getAccounts(ids = ids)
            .map { it.toDataModel() }
    }

    override fun getAllAccountsAsFlow(): Flow<List<AccountDataModel>> {
        return localSource
            .getAllAccountsAsFlow()
            .onStart { synchronizeAccounts() }
            .map { accounts ->
                accounts.map { it.toDataModel() }
            }
    }

    override suspend fun getAllAccounts(): List<AccountDataModel> {
        synchronizeAccounts()
        return localSource.getAllAccounts().map { it.toDataModel() }
    }

}