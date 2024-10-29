package com.ataglance.walletglance.recordAndAccount.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.account.data.model.AccountEntity
import com.ataglance.walletglance.record.data.model.RecordEntity

interface RecordAndAccountRepository {

    @Transaction
    suspend fun deleteRecordsAndUpsertAccounts(
        recordListToDelete: List<RecordEntity>,
        accountListToUpsert: List<AccountEntity>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    )

    @Transaction
    suspend fun deleteAndUpsertRecordsAndUpsertAccounts(
        recordListToDelete: List<RecordEntity>,
        recordListToUpsert: List<RecordEntity>,
        accountListToUpsert: List<AccountEntity>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    )

    @Transaction
    suspend fun upsertRecordsAndUpsertAccounts(
        recordListToUpsert: List<RecordEntity>,
        accountListToUpsert: List<AccountEntity>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    )

    @Transaction
    suspend fun deleteAndUpdateAccountsAndConvertTransfersToRecords(
        accountListToDelete: List<AccountEntity>,
        accountListToUpsert: List<AccountEntity>,
        onSuccessListener: () -> Unit,
        onFailureListener: (Exception) -> Unit
    )

}