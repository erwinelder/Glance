package com.ataglance.walletglance.account.data.remote.source

import com.ataglance.walletglance.account.data.remote.dao.AccountRemoteDao
import com.ataglance.walletglance.account.data.remote.model.AccountRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.dao.RemoteUpdateTimeDao

class AccountRemoteDataSourceImpl(
    private val accountDao: AccountRemoteDao,
    private val updateTimeDao: RemoteUpdateTimeDao
) : AccountRemoteDataSource {

    override suspend fun getUpdateTime(userId: String): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.Account.name, userId = userId)
    }

    override suspend fun saveUpdateTime(timestamp: Long, userId: String) {
        updateTimeDao.saveUpdateTime(
            tableName = TableName.Account.name, timestamp = timestamp, userId = userId
        )
    }

    override suspend fun upsertAccounts(
        accounts: List<AccountRemoteEntity>,
        timestamp: Long,
        userId: String
    ) {
        accountDao.upsertAccounts(accounts = accounts, userId = userId)
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun synchroniseAccounts(
        accountsToSync: EntitiesToSync<AccountRemoteEntity>,
        timestamp: Long,
        userId: String
    ) {
        accountDao.synchroniseAccounts(accountsToSync = accountsToSync, userId = userId)
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun getAccountsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<AccountRemoteEntity> {
        return accountDao.getAccountsAfterTimestamp(timestamp = timestamp, userId = userId)
    }

}