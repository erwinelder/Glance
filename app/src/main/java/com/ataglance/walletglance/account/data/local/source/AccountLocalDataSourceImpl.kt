package com.ataglance.walletglance.account.data.local.source

import com.ataglance.walletglance.account.data.local.dao.AccountLocalDao
import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow

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

    override suspend fun deleteUpdateTime() {
        updateTimeDao.deleteUpdateTime(tableName = TableName.Account.name)
    }

    override suspend fun saveAccounts(
        accounts: List<AccountEntity>,
        timestamp: Long
    ): List<AccountEntity> {
        return accountDao.saveAccounts(accounts = accounts).also {
            saveUpdateTime(timestamp = timestamp)
        }
    }

    override suspend fun deleteAccounts(accounts: List<AccountEntity>, timestamp: Long?) {
        accountDao.deleteAccounts(accounts = accounts)
        timestamp?.let { saveUpdateTime(timestamp = it) }
    }

    override suspend fun deleteAllAccounts() {
        accountDao.deleteAllAccounts()
        deleteUpdateTime()
    }

    override suspend fun deleteAndSaveAccounts(
        toDelete: List<AccountEntity>,
        toUpsert: List<AccountEntity>,
        timestamp: Long
    ): List<AccountEntity> {
        return accountDao.deleteAndSaveAccounts(toDelete = toDelete, toSave = toUpsert)
            .also { saveUpdateTime(timestamp = timestamp) }
    }

    override suspend fun getAccountsAfterTimestamp(timestamp: Long): List<AccountEntity> {
        return accountDao.getAccountsAfterTimestamp(timestamp = timestamp)
    }

    override fun getAllAccountsAsFlow(): Flow<List<AccountEntity>> {
        return accountDao.getAllAccountsAsFlow()
    }

    override suspend fun getAllAccounts(): List<AccountEntity> {
        return accountDao.getAllAccounts()
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
