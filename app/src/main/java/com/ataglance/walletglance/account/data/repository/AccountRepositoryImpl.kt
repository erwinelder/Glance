package com.ataglance.walletglance.account.data.repository

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.data.local.source.AccountLocalDataSource
import com.ataglance.walletglance.account.data.mapper.toLocalEntity
import com.ataglance.walletglance.account.data.mapper.toRemoteEntity
import com.ataglance.walletglance.account.data.remote.model.AccountRemoteEntity
import com.ataglance.walletglance.account.data.remote.source.AccountRemoteDataSource
import com.ataglance.walletglance.core.data.model.DataSyncHelper
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.utils.synchroniseData
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AccountRepositoryImpl(
    private val localSource: AccountLocalDataSource,
    private val remoteSource: AccountRemoteDataSource,
    private val syncHelper: DataSyncHelper
) : AccountRepository {

    private suspend fun syncDataFromRemote() {
        val userId = syncHelper.getUserIdForSync(TableName.Account) ?: return

        synchroniseData(
            localUpdateTimeGetter = localSource::getUpdateTime,
            remoteUpdateTimeGetter = { remoteSource.getUpdateTime(userId = userId) },
            remoteDataGetter = { timestamp ->
                remoteSource.getAccountsAfterTimestamp(timestamp = timestamp, userId = userId)
            },
            remoteDataToLocalDataMapper = AccountRemoteEntity::toLocalEntity,
            localSynchroniser = localSource::synchroniseAccounts
        )
    }


    override suspend fun upsertAccounts(accounts: List<AccountEntity>) {
        val timestamp = getCurrentTimestamp()

        localSource.upsertAccounts(accounts = accounts, timestamp = timestamp)
        syncHelper.tryToSyncToRemote(TableName.Account) { userId ->
            remoteSource.upsertAccounts(
                accounts = accounts.map {
                    it.toRemoteEntity(updateTime = timestamp, deleted = false)
                },
                timestamp = timestamp,
                userId = userId
            )
        }
    }

    override suspend fun deleteAndUpsertAccounts(
        toDelete: List<AccountEntity>,
        toUpsert: List<AccountEntity>
    ) {
        val timestamp = getCurrentTimestamp()
        val accountsToSync = EntitiesToSync(toDelete = toDelete, toUpsert = toUpsert)

        localSource.synchroniseAccounts(accountsToSync = accountsToSync, timestamp = timestamp)
        syncHelper.tryToSyncToRemote(TableName.Account) { userId ->
            remoteSource.synchroniseAccounts(
                accountsToSync = accountsToSync.map { deleted ->
                    toRemoteEntity(updateTime = timestamp, deleted = deleted)
                },
                timestamp = timestamp,
                userId = userId
            )
        }
    }

    override suspend fun deleteAllAccountsLocally() {
        val timestamp = getCurrentTimestamp()
        localSource.deleteAllAccounts(timestamp = timestamp)
    }

    override fun getAllAccounts(): Flow<List<AccountEntity>> = flow {
        syncDataFromRemote()
        localSource.getAllAccounts().collect(::emit)
    }

    override suspend fun getAccounts(ids: List<Int>): List<AccountEntity> {
        syncDataFromRemote()
        return localSource.getAccounts(ids = ids)
    }

    override suspend fun getAccount(id: Int): AccountEntity? {
        syncDataFromRemote()
        return localSource.getAccount(id = id)
    }

}