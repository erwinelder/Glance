package com.ataglance.walletglance.domain.repositories

import com.ataglance.walletglance.domain.dao.AccountDao
import com.ataglance.walletglance.domain.entities.AccountEntity
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