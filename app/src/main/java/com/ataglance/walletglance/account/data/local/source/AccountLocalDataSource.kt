package com.ataglance.walletglance.account.data.local.source

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import kotlinx.coroutines.flow.Flow

interface AccountLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun upsertAccounts(accounts: List<AccountEntity>, timestamp: Long)

    suspend fun deleteAllAccounts(timestamp: Long)

    suspend fun synchroniseAccounts(accountsToSync: EntitiesToSync<AccountEntity>, timestamp: Long)

    fun getAllAccountsFlow(): Flow<List<AccountEntity>>

    suspend fun getAllAccounts(): List<AccountEntity>

    suspend fun getAccounts(ids: List<Int>): List<AccountEntity>

    suspend fun getAccount(id: Int): AccountEntity?

}