package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.mapper.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAccountsUseCaseImpl(
    private val accountRepository: AccountRepository
) : GetAccountsUseCase {

    override fun getAllAsFlow(): Flow<List<Account>> {
        return accountRepository.getAllAccountsAsFlow().map { accounts ->
            accounts.map { it.toDomainModel() }
        }
    }

    override suspend fun getAll(): List<Account> {
        return accountRepository.getAllAccounts().map { it.toDomainModel() }
    }

    override suspend fun get(ids: List<Int>): List<Account> {
        return accountRepository.getAccounts(ids = ids).map { it.toDomainModel() }
    }

    override suspend fun get(id: Int): Account? {
        return accountRepository.getAccount(id = id)?.toDomainModel()
    }

}