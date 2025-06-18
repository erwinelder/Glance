package com.ataglance.walletglance.account.data.repository

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.domain.model.color.AccountColorName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AccountRepositoryMock : AccountRepository {

    var accounts = listOf(
        AccountEntity(
            id = 1,
            orderNum = 1,
            name = "Account 1",
            currency = "USD",
            balance = 500.0,
            color = AccountColorName.Default.name,
            hide = false,
            hideBalance = false,
            withoutBalance = false,
            isActive = true
        ),
        AccountEntity(
            id = 2,
            orderNum = 2,
            name = "Account 2",
            currency = "EUR",
            balance = 300.0,
            color = AccountColorName.Pink.name,
            hide = false,
            hideBalance = false,
            withoutBalance = false,
            isActive = false
        ),
    )


    override suspend fun upsertAccounts(accounts: List<AccountEntity>) {}

    override suspend fun deleteAndUpsertAccounts(
        toDelete: List<AccountEntity>,
        toUpsert: List<AccountEntity>
    ) {}

    override suspend fun deleteAllAccountsLocally() {}

    override fun getAllAccountsFlow(): Flow<List<AccountEntity>> = flow {
        emit(accounts)
    }

    override suspend fun getAllAccounts(): List<AccountEntity> {
        return accounts
    }

    override suspend fun getAccounts(ids: List<Int>): List<AccountEntity> {
        return accounts.filter { it.id in ids }
    }

    override suspend fun getAccount(id: Int): AccountEntity? {
        return accounts.find { it.id == id }
    }

}