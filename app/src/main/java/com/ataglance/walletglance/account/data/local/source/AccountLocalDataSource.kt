package com.ataglance.walletglance.account.data.local.source

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSynchronise
import kotlinx.coroutines.flow.Flow

interface AccountLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun upsertAccounts(accounts: List<AccountEntity>, timestamp: Long)

    suspend fun deleteAllAccounts(timestamp: Long)

    suspend fun synchroniseAccounts(
        accountsToSync: EntitiesToSynchronise<AccountEntity>,
        timestamp: Long
    )

    fun getAllAccounts(): Flow<List<AccountEntity>>

}