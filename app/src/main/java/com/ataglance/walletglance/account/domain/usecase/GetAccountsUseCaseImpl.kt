package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.mapper.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAccountsUseCaseImpl(
    private val accountRepository: AccountRepository
) : GetAccountsUseCase {

    override fun getAllAsFlow(): Flow<List<Account>> {
        return accountRepository.getAllAccountsFlow().map { it.map(AccountEntity::toDomainModel) }
    }

    override suspend fun getAll(): List<Account> {
        return accountRepository.getAllAccounts().map(AccountEntity::toDomainModel)
    }

    override suspend fun get(ids: List<Int>): List<Account> {
        return accountRepository.getAccounts(ids = ids).map(AccountEntity::toDomainModel)
    }

    override suspend fun get(id: Int): Account? {
        return accountRepository.getAccount(id = id)?.toDomainModel()
    }

    override suspend fun getActive(): Account? {
        return accountRepository.getAllAccounts().firstOrNull()?.toDomainModel()
    }

}