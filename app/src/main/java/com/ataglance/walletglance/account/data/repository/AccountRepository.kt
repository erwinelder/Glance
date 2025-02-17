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

    fun getAllAccountsFlow(): Flow<List<AccountEntity>>

    suspend fun getAllAccounts(): List<AccountEntity>

    suspend fun getAccounts(ids: List<Int>): List<AccountEntity>

    suspend fun getAccount(id: Int): AccountEntity?

}