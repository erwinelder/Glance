package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.data.model.AccountDataModel
import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.mapper.toDataModel
import com.ataglance.walletglance.core.utils.asList
import com.ataglance.walletglance.core.utils.excludeItems
import com.ataglance.walletglance.transaction.domain.usecase.TransformAccountTransactionsToRecords

class SaveAccountsUseCaseImpl(
    private val accountRepository: AccountRepository,
    private val transformAccountTransactionsToRecords: TransformAccountTransactionsToRecords
) : SaveAccountsUseCase {

    override suspend fun saveAndDeleteRest(accounts: List<Account>) {
        val currentAccounts = accountRepository.getAllAccounts()

        val accountsToSave = accounts.map { it.toDataModel() }
        val accountsToDelete = currentAccounts.excludeItems(accountsToSave) { it.id }
        val accountIdsToDelete = accountsToDelete.map { it.id }

        if (accountsToDelete.isNotEmpty()) {
            transformAccountTransactionsToRecords.execute(accountIds = accountIdsToDelete)
        }
        accountRepository.deleteAndUpsertAccounts(
            toDelete = accountsToDelete, toUpsert = accountsToSave
        )
    }

    override suspend fun save(accounts: List<AccountDataModel>) {
        accountRepository.upsertAccounts(accounts = accounts)
    }

    override suspend fun save(account: AccountDataModel) {
        accountRepository.upsertAccounts(accounts = account.asList())
    }

}