package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.mapper.toDataModel
import com.ataglance.walletglance.budget.domain.usecase.DeleteBudgetAccountAssociationsByAccountsUseCase
import com.ataglance.walletglance.core.utils.excludeItems
import com.ataglance.walletglance.record.data.repository.RecordRepository

class SaveAccountsUseCaseImpl(
    private val accountRepository: AccountRepository,
    private val recordRepository: RecordRepository,
    private val deleteBudgetAccountAssociationsByAccountsUseCase:
    DeleteBudgetAccountAssociationsByAccountsUseCase
) : SaveAccountsUseCase {

    override suspend fun save(accounts: List<Account>) {
        val currentAccounts = accountRepository.getAllAccounts()

        val accountsToSave = accounts.map(Account::toDataModel)
        val accountsToDelete = currentAccounts.excludeItems(accountsToSave) { it.id }
        val accountIdsToDelete = accountsToDelete.map { it.id }

        if (accountsToDelete.isNotEmpty()) {
            deleteBudgetAccountAssociationsByAccountsUseCase.delete(accountIds = accountIdsToDelete)
            recordRepository.deleteRecordsByAccounts(accountIds = accountIdsToDelete)
        }
        accountRepository.deleteAndUpsertAccounts(
            toDelete = accountsToDelete, toUpsert = accountsToSave
        )
    }

    override suspend fun upsert(accounts: List<AccountEntity>) {
        accountRepository.upsertAccounts(accounts = accounts)
    }

}