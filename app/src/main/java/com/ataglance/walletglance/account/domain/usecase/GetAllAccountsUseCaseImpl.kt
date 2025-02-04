package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.mapper.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class GetAllAccountsUseCaseImpl(
    private val accountRepository: AccountRepository
) : GetAllAccountsUseCase {

    override fun getAsFlow(): Flow<List<Account>> {
        return accountRepository.getAllAccounts().map { it.map(AccountEntity::toDomainModel) }
    }

    override suspend fun get(): List<Account> {
        return accountRepository.getAllAccounts().firstOrNull()
            ?.map(AccountEntity::toDomainModel)
            ?: emptyList()
    }

}