package com.ataglance.walletglance.account.domain.usecase

import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.account.data.utils.getThatAreNotInList
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.mapper.toDataModel
import com.ataglance.walletglance.record.data.repository.RecordRepository

class SaveAccountsUseCaseImpl(
    private val accountRepository: AccountRepository,
    private val recordRepository: RecordRepository
) : SaveAccountsUseCase {
    override suspend fun execute(accountsToSave: List<Account>, currentAccounts: List<Account>) {
        val entitiesToSave = accountsToSave.map(Account::toDataModel)
        val entitiesToDelete = currentAccounts
            .map(Account::toDataModel)
            .getThatAreNotInList(entitiesToSave)

        if (entitiesToDelete.isEmpty()) {
            accountRepository.deleteAndUpsertAccounts(
                toDelete = entitiesToDelete, toUpsert = entitiesToSave
            )
        } else {
            recordRepository.convertRecordsToTransfers(
                noteValues = entitiesToDelete.map { it.id.toString() }
            )
        }
    }
}