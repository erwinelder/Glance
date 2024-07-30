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
    suspend fun upsertRecordsAndUpsertAccounts(
        recordList: List<Record>,
        accountList: List<AccountEntity>
    ) {
        recordDao.upsertRecords(recordList)
        accountDao.upsertAccounts(accountList)
    }

    @Transaction
    suspend fun deleteAndUpdateAccountsAndDeleteRecordsByAccountIdAndConvertTransfersToRecords(
        accountsIdsToDelete: List<Int>,
        accountListToUpsert: List<AccountEntity>
    ) {
        accountDao.deleteAccountsByIds(accountsIdsToDelete)
        accountDao.upsertAccounts(accountListToUpsert)
        recordDao.convertTransfersToRecords(accountsIdsToDelete.map { it.toString() })
    }

}