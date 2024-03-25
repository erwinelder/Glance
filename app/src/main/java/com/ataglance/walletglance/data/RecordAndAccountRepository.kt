package com.ataglance.walletglance.data

import androidx.room.Transaction

class RecordAndAccountRepository(
    private val recordDao: RecordDao,
    private val accountDao: AccountDao
) {

    @Transaction
    suspend fun upsertRecordsAndUpdateAccounts(
        recordList: List<Record>,
        accountList: List<Account>
    ) {
        recordDao.insertOrReplaceRecords(recordList)
        accountDao.insertOrReplaceAccounts(accountList)
    }

    @Transaction
    suspend fun deleteAndUpsertRecordsAndUpdateAccounts(
        recordListToDelete: List<Record>,
        recordListToUpsert: List<Record>,
        accountList: List<Account>
    ) {
        recordDao.deleteRecords(recordListToDelete)
        recordDao.insertOrReplaceRecords(recordListToUpsert)
        accountDao.insertOrReplaceAccounts(accountList)
    }

    @Transaction
    suspend fun deleteRecordsAndUpdateAccounts(
        recordList: List<Record>,
        accountList: List<Account>
    ) {
        recordDao.deleteRecords(recordList)
        accountDao.insertOrReplaceAccounts(accountList)
    }

    @Transaction
    suspend fun deleteAccountAndUpdateAccountsAndDeleteRecordsByAccountIdAndUpdateRecords(
        accountIdToDelete: Int,
        accountListToUpsert: List<Account>,
        recordListToUpsert: List<Record>
    ) {
        accountDao.insertOrReplaceAccounts(accountListToUpsert)
        accountDao.deleteAccountById(accountIdToDelete)
        recordDao.deleteRecordsByAccountId(accountIdToDelete)
        recordDao.insertOrReplaceRecords(recordListToUpsert)
    }

}