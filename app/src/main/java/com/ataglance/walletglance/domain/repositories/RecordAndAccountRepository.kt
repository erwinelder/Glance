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
    suspend fun upsertRecordsAndUpdateAccounts(
        recordList: List<Record>,
        accountList: List<AccountEntity>
    ) {
        recordDao.upsertRecords(recordList)
        accountDao.upsertAccounts(accountList)
    }

    @Transaction
    suspend fun deleteAndUpsertRecordsAndUpdateAccounts(
        recordListToDelete: List<Record>,
        recordListToUpsert: List<Record>,
        accountList: List<AccountEntity>
    ) {
        recordDao.deleteRecords(recordListToDelete)
        recordDao.upsertRecords(recordListToUpsert)
        accountDao.upsertAccounts(accountList)
    }

    @Transaction
    suspend fun deleteRecordsAndUpdateAccounts(
        recordList: List<Record>,
        accountList: List<AccountEntity>
    ) {
        recordDao.deleteRecords(recordList)
        accountDao.upsertAccounts(accountList)
    }

    @Transaction
    suspend fun deleteAndUpdateAccountsAndDeleteRecordsByAccountIdAndConvertTransfersToRecords(
        accountIdToDelete: List<Int>,
        accountListToUpsert: List<AccountEntity>
    ) {
        accountDao.deleteAccountsByIds(accountIdToDelete)
        accountDao.upsertAccounts(accountListToUpsert)
        recordDao.deleteRecordsByAccountIds(accountIdToDelete)
        recordDao.convertTransfersToRecords(accountIdToDelete.map { it.toString() })
    }

}