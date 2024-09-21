package com.ataglance.walletglance.account.data.repository

import com.ataglance.walletglance.account.data.local.dao.AccountDao
import com.ataglance.walletglance.account.data.local.model.AccountEntity
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