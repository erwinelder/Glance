package com.ataglance.walletglance.account.data.local.source

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import kotlinx.coroutines.flow.Flow

interface AccountLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun deleteUpdateTime()

    suspend fun saveAccounts(accounts: List<AccountEntity>, timestamp: Long): List<AccountEntity>

    suspend fun deleteAccounts(accounts: List<AccountEntity>, timestamp: Long? = null)

    suspend fun deleteAllAccounts()

    suspend fun deleteAndSaveAccounts(
        toDelete: List<AccountEntity>,
        toUpsert: List<AccountEntity>,
        timestamp: Long
    ): List<AccountEntity>

    suspend fun getAccountsAfterTimestamp(timestamp: Long): List<AccountEntity>

    fun getAllAccountsAsFlow(): Flow<List<AccountEntity>>

    suspend fun getAllAccounts(): List<AccountEntity>

    suspend fun getAccounts(ids: List<Int>): List<AccountEntity>

    suspend fun getAccount(id: Int): AccountEntity?

}