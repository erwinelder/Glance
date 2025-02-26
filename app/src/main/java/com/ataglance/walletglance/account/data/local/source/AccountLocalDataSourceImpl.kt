package com.ataglance.walletglance.account.data.local.source

import com.ataglance.walletglance.account.data.local.dao.AccountLocalDao
import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class AccountLocalDataSourceImpl(
    private val accountDao: AccountLocalDao,
    private val updateTimeDao: LocalUpdateTimeDao
) : AccountLocalDataSource {

    override suspend fun getUpdateTime(): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.Account.name)
    }

    override suspend fun saveUpdateTime(timestamp: Long) {
        updateTimeDao.saveUpdateTime(tableName = TableName.Account.name, timestamp = timestamp)
    }

    override suspend fun upsertAccounts(accounts: List<AccountEntity>, timestamp: Long) {
        accountDao.upsertAccounts(accounts = accounts)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun deleteAllAccounts(timestamp: Long) {
        accountDao.deleteAllAccounts()
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun synchroniseAccounts(
        accountsToSync: EntitiesToSync<AccountEntity>,
        timestamp: Long
    ) {
        accountDao.deleteAndUpsertAccounts(
            toDelete = accountsToSync.toDelete,
            toUpsert = accountsToSync.toUpsert
        )
        saveUpdateTime(timestamp = timestamp)
    }

    override fun getAllAccountsFlow(): Flow<List<AccountEntity>> {
        return accountDao.getAllAccounts()
    }

    override suspend fun getAllAccounts(): List<AccountEntity> {
        return accountDao.getAllAccounts().firstOrNull().orEmpty()
    }

    override suspend fun getAccounts(ids: List<Int>): List<AccountEntity> {
        return accountDao.getAccounts(ids = ids)
    }

    override suspend fun getAccount(id: Int): AccountEntity? {
        return accountDao.getAccount(id = id)
    }

}

fun getAccountLocalDataSource(appDatabase: AppDatabase): AccountLocalDataSource {
    return AccountLocalDataSourceImpl(
        accountDao = appDatabase.accountDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}