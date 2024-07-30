package com.ataglance.walletglance.domain.repositories

import androidx.room.Transaction
import com.ataglance.walletglance.domain.dao.AccountDao
import com.ataglance.walletglance.domain.dao.BudgetDao
import com.ataglance.walletglance.domain.dao.RecordDao
import com.ataglance.walletglance.domain.entities.AccountEntity
import com.ataglance.walletglance.domain.entities.BudgetEntity
import com.ataglance.walletglance.domain.entities.Record

class RecordAndAccountAndBudgetRepository(
    private val recordDao: RecordDao,
    private val accountDao: AccountDao,
    private val budgetDao: BudgetDao
) {

    @Transaction
    suspend fun upsertRecordsAndUpsertAccountsAnsUpsertBudgets(
        recordList: List<Record>,
        accountList: List<AccountEntity>,
        budgetList: List<BudgetEntity>
    ) {
        recordDao.upsertRecords(recordList)
        accountDao.upsertAccounts(accountList)
        if (budgetList.isNotEmpty()) budgetDao.upsertBudgets(budgetList)
    }

    @Transaction
    suspend fun deleteAndUpsertRecordsAndUpsertAccountsAndUpsertBudgets(
        recordListToDelete: List<Record>,
        recordListToUpsert: List<Record>,
        accountList: List<AccountEntity>,
        budgetList: List<BudgetEntity>
    ) {
        recordDao.deleteRecords(recordListToDelete)
        recordDao.upsertRecords(recordListToUpsert)
        accountDao.upsertAccounts(accountList)
        if (budgetList.isNotEmpty()) budgetDao.upsertBudgets(budgetList)
    }

    @Transaction
    suspend fun deleteRecordsAndUpsertAccountsAndUpsertBudgets(
        recordList: List<Record>,
        accountList: List<AccountEntity>,
        budgetList: List<BudgetEntity>
    ) {
        recordDao.deleteRecords(recordList)
        accountDao.upsertAccounts(accountList)
        if (budgetList.isNotEmpty()) budgetDao.upsertBudgets(budgetList)
    }

}