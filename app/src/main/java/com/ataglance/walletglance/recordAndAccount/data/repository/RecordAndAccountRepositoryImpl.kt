package com.ataglance.walletglance.recordAndAccount.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.account.data.local.model.AccountEntity
import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.record.data.model.RecordEntity
import com.ataglance.walletglance.record.data.repository.RecordRepository

class RecordAndAccountRepositoryImpl(
    private val recordRepository: RecordRepository,
    private val accountRepository: AccountRepository
) : RecordAndAccountRepository {

    @Transaction
    override suspend fun deleteRecordsAndUpsertAccounts(
        recordListToDelete: List<RecordEntity>,
        accountListToUpsert: List<AccountEntity>
    ) {
        recordRepository.deleteAndUpsertEntities(
            toDelete = recordListToDelete, toUpsert = emptyList()
        )
        accountRepository.upsertAccounts(accounts = accountListToUpsert)
    }

    @Transaction
    override suspend fun deleteAndUpsertRecordsAndUpsertAccounts(
        recordListToDelete: List<RecordEntity>,
        recordListToUpsert: List<RecordEntity>,
        accountListToUpsert: List<AccountEntity>
    ) {
        recordRepository.deleteAndUpsertEntities(
            toDelete = recordListToDelete, toUpsert = recordListToUpsert
        )
        accountRepository.upsertAccounts(accounts = accountListToUpsert)
    }

    @Transaction
    override suspend fun upsertRecordsAndUpsertAccounts(
        recordListToUpsert: List<RecordEntity>,
        accountListToUpsert: List<AccountEntity>
    ) {
        recordRepository.upsertEntities(entityList = recordListToUpsert)
        accountRepository.upsertAccounts(accounts = accountListToUpsert)
    }

    /*@Transaction
    override suspend fun deleteAndUpdateAccountsAndConvertTransfersToRecords(
        accountListToDelete: List<AccountEntity>,
        accountListToUpsert: List<AccountEntity>
    ) {
        accountRepository.deleteAndUpsertEntities(
            toDelete = accountListToDelete,
            toUpsert = accountListToUpsert
        )
        recordRepository.convertRecordsToTransfers(
            noteValues = accountListToDelete.map { it.id.toString() }
        )
    }*/

}