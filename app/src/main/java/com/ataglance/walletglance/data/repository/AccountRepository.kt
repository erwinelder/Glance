package com.ataglance.walletglance.data.repository

import com.ataglance.walletglance.data.local.dao.AccountDao
import com.ataglance.walletglance.data.local.entities.AccountEntity
import kotlinx.coroutines.flow.Flow

class AccountRepository(
    private val dao: AccountDao
) {

    suspend fun upsertAccounts(accountList: List<AccountEntity>) {
        dao.upsertAccounts(accountList)
    }

    suspend fun deleteAllAccounts() {
        dao.deleteAllAccounts()
    }

    fun getAllAccounts(): Flow<List<AccountEntity>> {
        return dao.getAllAccounts()
    }

}