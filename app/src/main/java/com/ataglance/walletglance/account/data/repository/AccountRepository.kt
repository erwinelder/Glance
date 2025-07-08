package com.ataglance.walletglance.account.data.repository

import com.ataglance.walletglance.account.data.model.AccountDataModel
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun upsertAccounts(accounts: List<AccountDataModel>)

    suspend fun deleteAndUpsertAccounts(
        toDelete: List<AccountDataModel>,
        toUpsert: List<AccountDataModel>
    )

    suspend fun deleteAllAccountsLocally()

    suspend fun getAccount(id: Int): AccountDataModel?

    suspend fun getAccounts(ids: List<Int>): List<AccountDataModel>

    fun getAllAccountsAsFlow(): Flow<List<AccountDataModel>>

    suspend fun getAllAccounts(): List<AccountDataModel>

}