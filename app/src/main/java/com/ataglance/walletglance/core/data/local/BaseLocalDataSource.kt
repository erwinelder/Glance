package com.ataglance.walletglance.core.data.local

import com.ataglance.walletglance.account.data.local.AccountDao
import com.ataglance.walletglance.account.data.model.AccountEntity
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.utils.getNowDateTimeLong
import kotlinx.coroutines.flow.Flow

class BaseLocalDataSource<T>(
    private val accountDao: AccountDao,
    private val updateTimeDao: TableUpdateTimeDao,
    private val tableName: TableName
) {

    private suspend fun updateTime() {
        updateTimeDao.updateTime(tableName.name, getNowDateTimeLong())
    }

    suspend fun upsertAccounts(accountList: List<AccountEntity>) {
        accountDao.upsertAccounts(accountList)
        updateTime()
    }

    suspend fun deleteAccountsByIds(idList: List<Int>) {
        accountDao.deleteAccountsByIds(idList)
        updateTime()
    }

    suspend fun deleteAllAccounts() {
        accountDao.deleteAllAccounts()
        updateTime()
    }

    fun getAllAccounts(): Flow<List<AccountEntity>> {
        return accountDao.getAllAccounts()
    }

}