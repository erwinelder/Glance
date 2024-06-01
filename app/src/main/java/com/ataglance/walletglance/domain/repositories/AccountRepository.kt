package com.ataglance.walletglance.domain.repositories

import com.ataglance.walletglance.domain.dao.AccountDao
import com.ataglance.walletglance.domain.entities.Account
import kotlinx.coroutines.flow.Flow

class AccountRepository(
    private val dao: AccountDao
) {

    suspend fun upsertAccounts(accountList: List<Account>) {
        dao.insertOrReplaceAccounts(accountList)
    }

    suspend fun deleteAllAccounts() {
        dao.deleteAllAccounts()
    }

    suspend fun deleteAndUpsertAccounts(idListToDelete: List<Int>, accountList: List<Account>) {
        dao.deleteAccountsByIds(idListToDelete)
        dao.insertOrReplaceAccounts(accountList)
    }

    fun getAllAccounts(): Flow<List<Account>> {
        return dao.getAllAccounts()
    }

}