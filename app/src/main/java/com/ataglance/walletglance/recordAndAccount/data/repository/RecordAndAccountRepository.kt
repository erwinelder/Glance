package com.ataglance.walletglance.recordAndAccount.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.account.data.local.AccountDao
import com.ataglance.walletglance.account.data.model.AccountEntity
import com.ataglance.walletglance.record.data.local.RecordDao
import com.ataglance.walletglance.record.data.model.RecordEntity

class RecordAndAccountRepository(
    private val recordDao: RecordDao,
    private val accountDao: AccountDao
) {

    @Transaction
    suspend fun deleteAndUpsertRecordsAndUpsertAccounts(
        recordListToDelete: List<RecordEntity>,
        recordListToUpsert: List<RecordEntity>,
        accountListToUpsert: List<AccountEntity>
    ) {
        if (recordListToDelete.isNotEmpty())
            recordDao.deleteRecords(recordListToDelete)
        recordDao.upsertRecords(recordListToUpsert)
        accountDao.upsertAccounts(accountListToUpsert)
    }

    @Transaction
    suspend fun upsertRecordsAndUpsertAccounts(
        recordListToUpsert: List<RecordEntity>,
        accountListToUpsert: List<AccountEntity>
    ) {
        recordDao.upsertRecords(recordListToUpsert)
        accountDao.upsertAccounts(accountListToUpsert)
    }

    @Transaction
    suspend fun deleteRecordsAndUpsertAccounts(
        recordListToDelete: List<RecordEntity>,
        accountListToUpsert: List<AccountEntity>
    ) {
        recordDao.deleteRecords(recordListToDelete)
        accountDao.upsertAccounts(accountListToUpsert)
    }

    @Transaction
    suspend fun deleteAndUpdateAccountsAndDeleteRecordsByAccountIdAndConvertTransfersToRecords(
        accountsIdsToDelete: List<Int>,
        accountListToUpsert: List<AccountEntity>
    ) {
        accountDao.deleteAccountsByIds(accountsIdsToDelete)
        accountDao.upsertAccounts(accountListToUpsert)
        recordDao.convertTransfersToRecords(noteValues = accountsIdsToDelete.map { it.toString() })
    }

}