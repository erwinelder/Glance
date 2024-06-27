package com.ataglance.walletglance.domain.repositories

import com.ataglance.walletglance.domain.dao.AccountDao
import com.ataglance.walletglance.domain.entities.AccountEntity
import kotlinx.coroutines.flow.Flow

class AccountRepository(
    private val dao: AccountDao
) {

    suspend fun upsertAccounts(accountList: List<AccountEntity>) {
        dao.insertOrReplaceAccounts(accountList)
    }

    suspend fun deleteAllAccounts() {
        dao.deleteAllAccounts()
    }

    suspend fun deleteAndUpsertAccounts(idListToDelete: List<Int>, accountList: List<AccountEntity>) {
        dao.deleteAccountsByIds(idListToDelete)
        dao.insertOrReplaceAccounts(accountList)
    }

    fun getAllAccounts(): Flow<List<AccountEntity>> {
        return dao.getAllAccounts()
    }

}