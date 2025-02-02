package com.ataglance.walletglance.account.data.local.source

import com.ataglance.walletglance.account.data.local.dao.AccountDao
import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.model.EntitiesToSynchronise
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow

class AccountLocalDataSourceImpl(
    private val accountDao: AccountDao,
    private val updateTimeDao: LocalUpdateTimeDao
) : AccountLocalDataSource {

    override suspend fun getUpdateTime(): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.Account.name)
    }

    override suspend fun saveUpdateTime(timestamp: Long) {
        updateTimeDao.saveUpdateTime(tableName = TableName.Account.name, timestamp = timestamp)
    }

    override suspend fun upsertAccounts(accounts: List<AccountEntity>, timestamp: Long) {
        accountDao.upsertEntities(entities = accounts)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun deleteAllAccounts(timestamp: Long) {
        accountDao.deleteAllAccounts()
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun synchroniseAccounts(
        accountsToSync: EntitiesToSynchronise<AccountEntity>,
        timestamp: Long
    ) {
        accountDao.deleteAndUpsertEntities(
            toDelete = accountsToSync.toDelete,
            toUpsert = accountsToSync.toUpsert
        )
        saveUpdateTime(timestamp = timestamp)
    }

    override fun getAllAccounts(): Flow<List<AccountEntity>> {
        return accountDao.getAllAccounts()
    }

}

fun getAccountLocalDataSource(appDatabase: AppDatabase): AccountLocalDataSource {
    return AccountLocalDataSourceImpl(
        accountDao = appDatabase.accountDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}
