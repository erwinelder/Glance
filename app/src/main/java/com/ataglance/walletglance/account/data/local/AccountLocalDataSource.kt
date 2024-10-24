package com.ataglance.walletglance.account.data.local

import com.ataglance.walletglance.account.data.model.AccountEntity
import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.TableUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName
import kotlinx.coroutines.flow.Flow

class AccountLocalDataSource(
    private val accountDao: AccountDao,
    updateTimeDao: TableUpdateTimeDao
) : BaseLocalDataSource(
    updateTimeDao = updateTimeDao,
    tableName = TableName.Account
) {

    suspend fun upsertAccounts(accountList: List<AccountEntity>, timestamp: Long) {
        accountDao.upsertAccounts(accountList)
        updateTime(timestamp)
    }

    suspend fun deleteAccountsByIds(idList: List<Int>, timestamp: Long) {
        accountDao.deleteAccountsByIds(idList)
        updateTime(timestamp)
    }

    suspend fun deleteAllAccounts(timestamp: Long) {
        accountDao.deleteAllAccounts()
        updateTime(timestamp)
    }

    fun getAllAccounts(): Flow<List<AccountEntity>> {
        return accountDao.getAllAccounts()
    }

}