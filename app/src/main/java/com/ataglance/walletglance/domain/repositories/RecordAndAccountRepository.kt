package com.ataglance.walletglance.domain.repositories

import androidx.room.Transaction
import com.ataglance.walletglance.domain.dao.AccountDao
import com.ataglance.walletglance.domain.dao.RecordDao
import com.ataglance.walletglance.domain.entities.AccountEntity
import com.ataglance.walletglance.domain.entities.Record

class RecordAndAccountRepository(
    private val recordDao: RecordDao,
    private val accountDao: AccountDao
) {

    @Transaction
    suspend fun deleteAndUpsertRecordsAndUpsertAccounts(
        recordListToDelete: List<Record>,
        recordListToUpsert: List<Record>,
        accountListToUpsert: List<AccountEntity>
    ) {
        if (recordListToDelete.isNotEmpty()) recordDao.deleteRecords(recordListToDelete)
        recordDao.upsertRecords(recordListToUpsert)
        accountDao.upsertAccounts(accountListToUpsert)
    }

    @Transaction
    suspend fun upsertRecordsAndUpsertAccounts(
        recordListToUpsert: List<Record>,
        accountListToUpsert: List<AccountEntity>
    ) {
        recordDao.upsertRecords(recordListToUpsert)
        accountDao.upsertAccounts(accountListToUpsert)
    }

    @Transaction
    suspend fun deleteRecordsAndUpsertAccounts(
        recordListToDelete: List<Record>,
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