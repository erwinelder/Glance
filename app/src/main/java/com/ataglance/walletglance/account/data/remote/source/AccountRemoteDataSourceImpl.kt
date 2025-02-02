package com.ataglance.walletglance.account.data.remote.source

import com.ataglance.walletglance.account.data.remote.dao.AccountRemoteDao
import com.ataglance.walletglance.account.data.remote.model.AccountRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSynchronise
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
        accountDao.upsertEntities(entities = accounts, userId = userId)
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun synchroniseAccounts(
        accountsToSync: EntitiesToSynchronise<AccountRemoteEntity>,
        timestamp: Long,
        userId: String
    ) {
        accountDao.synchroniseEntities(entitiesToSync = accountsToSync, userId = userId)
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun getAccountsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSynchronise<AccountRemoteEntity> {
        return accountDao.getEntitiesAfterTimestamp(timestamp = timestamp, userId = userId)
    }

}