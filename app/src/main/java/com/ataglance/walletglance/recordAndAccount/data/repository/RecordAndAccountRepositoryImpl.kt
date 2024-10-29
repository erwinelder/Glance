package com.ataglance.walletglance.recordAndAccount.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.account.data.model.AccountEntity
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
        accountListToUpsert: List<AccountEntity>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        recordRepository.deleteAndUpsertEntities(
            toDelete = recordListToDelete,
            toUpsert = emptyList(),
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
        accountRepository.upsertEntities(
            entityList = accountListToUpsert,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
    }

    @Transaction
    override suspend fun deleteAndUpsertRecordsAndUpsertAccounts(
        recordListToDelete: List<RecordEntity>,
        recordListToUpsert: List<RecordEntity>,
        accountListToUpsert: List<AccountEntity>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        recordRepository.deleteAndUpsertEntities(
            toDelete = recordListToDelete,
            toUpsert = recordListToUpsert,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
        accountRepository.upsertEntities(
            entityList = accountListToUpsert,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
    }

    @Transaction
    override suspend fun upsertRecordsAndUpsertAccounts(
        recordListToUpsert: List<RecordEntity>,
        accountListToUpsert: List<AccountEntity>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        recordRepository.upsertEntities(
            entityList = recordListToUpsert,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
        accountRepository.upsertEntities(
            entityList = accountListToUpsert,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
    }

    @Transaction
    override suspend fun deleteAndUpdateAccountsAndConvertTransfersToRecords(
        accountListToDelete: List<AccountEntity>,
        accountListToUpsert: List<AccountEntity>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    ) {
        accountRepository.deleteAndUpsertEntities(
            toDelete = accountListToDelete,
            toUpsert = accountListToUpsert,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
        recordRepository.convertRecordsToTransfers(
            noteValues = accountListToDelete.map { it.id.toString() },
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
    }

}