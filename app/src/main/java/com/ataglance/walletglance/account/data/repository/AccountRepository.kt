package com.ataglance.walletglance.account.data.repository

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun upsertAccounts(accounts: List<AccountEntity>)

    suspend fun deleteAndUpsertAccounts(
        toDelete: List<AccountEntity>,
        toUpsert: List<AccountEntity>
    )

    suspend fun deleteAllAccountsLocally()

    fun getAllAccounts(): Flow<List<AccountEntity>>

}