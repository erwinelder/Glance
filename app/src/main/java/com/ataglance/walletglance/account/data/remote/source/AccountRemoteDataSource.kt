package com.ataglance.walletglance.account.data.remote.source

import com.ataglance.walletglance.account.data.remote.model.AccountRemoteEntity
import com.ataglance.walletglance.core.data.model.EntitiesToSynchronise

interface AccountRemoteDataSource {

    suspend fun getUpdateTime(userId: String): Long?

    suspend fun saveUpdateTime(timestamp: Long, userId: String)

    suspend fun upsertAccounts(accounts: List<AccountRemoteEntity>, timestamp: Long, userId: String)

    suspend fun synchroniseAccounts(
        accountsToSync: EntitiesToSynchronise<AccountRemoteEntity>,
        timestamp: Long,
        userId: String
    )

    suspend fun getAccountsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSynchronise<AccountRemoteEntity>

}