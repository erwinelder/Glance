package com.ataglance.walletglance.account.data.repository

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.data.local.source.AccountLocalDataSource
import com.ataglance.walletglance.account.data.mapper.toLocalEntity
import com.ataglance.walletglance.account.data.mapper.toRemoteEntity
import com.ataglance.walletglance.account.data.remote.model.AccountRemoteEntity
import com.ataglance.walletglance.account.data.remote.source.AccountRemoteDataSource
import com.ataglance.walletglance.auth.data.model.UserContext
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.utils.synchroniseData
import com.ataglance.walletglance.core.utils.getCurrentTimestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AccountRepositoryImpl(
    private val localSource: AccountLocalDataSource,
    private val remoteSource: AccountRemoteDataSource,
    private val userContext: UserContext
) : AccountRepository {

    private suspend fun synchroniseAccounts() {
        val userId = userContext.getUserId() ?: return

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
        userContext.getUserId()?.let { userId ->
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
        userContext.getUserId()?.let { userId ->
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
        synchroniseAccounts()
        localSource.getAllAccounts().collect(::emit)
    }

}