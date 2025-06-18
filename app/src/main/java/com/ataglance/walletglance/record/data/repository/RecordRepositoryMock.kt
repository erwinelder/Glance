package com.ataglance.walletglance.record.data.repository

import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.model.RecordType
import com.ataglance.walletglance.record.domain.utils.asChar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecordRepositoryMock : RecordRepository {

    var records = listOf(
        RecordEntity(
            id = 1,
            recordNum = 1,
            date = 202506172155,
            type = RecordType.Expense.asChar(),
            accountId = 1,
            amount = 26.27,
            quantity = 1,
            categoryId = 1,
            subcategoryId = 1,
            note = "item 1",
            includeInBudgets = true
        ),
        RecordEntity(
            id = 2,
            recordNum = 2,
            date = 202506172155,
            type = RecordType.Expense.asChar(),
            accountId = 1,
            amount = 10.0,
            quantity = 1,
            categoryId = 2,
            subcategoryId = 2,
            note = "item 2",
            includeInBudgets = true
        ),
        RecordEntity(
            id = 3,
            recordNum = 3,
            date = 202506172155,
            type = RecordType.Income.asChar(),
            accountId = 1,
            amount = 100.0,
            quantity = 1,
            categoryId = 72,
            subcategoryId = null,
            note = "salary",
            includeInBudgets = true
        ),
    )


    override suspend fun upsertRecords(records: List<RecordEntity>) {}

    override suspend fun deleteRecords(records: List<RecordEntity>) {}

    override suspend fun deleteAndUpsertRecords(
        toDelete: List<RecordEntity>,
        toUpsert: List<RecordEntity>
    ) {}

    override suspend fun deleteAllRecordsLocally() {}

    override suspend fun deleteRecordsByAccounts(accountIds: List<Int>) {}

    override fun getLastRecordNumFlow(): Flow<Int?> = flow {
        val lastRecordNum = records.maxOfOrNull { it.recordNum }
        emit(lastRecordNum)
    }

    override suspend fun getLastRecordNum(): Int? {
        return records.maxOfOrNull { it.recordNum }
    }

    override suspend fun getLastRecordsByTypeAndAccount(
        type: Char,
        accountId: Int
    ): List<RecordEntity> {
        return records
            .filter { it.type == type && it.accountId == accountId }
            .sortedByDescending { it.date }
    }

    override suspend fun getRecordsByRecordNum(recordNum: Int): List<RecordEntity> {
        return records
            .filter { it.recordNum == recordNum }
            .sortedByDescending { it.date }
    }

    override fun getRecordsInDateRangeFlow(range: LongDateRange): Flow<List<RecordEntity>> = flow {
        val filteredRecords = records.filter { range.containsDate(date = it.date) }
        emit(filteredRecords)
    }

    override suspend fun getRecordsInDateRange(range: LongDateRange): List<RecordEntity> {
        return records.filter { range.containsDate(date = it.date) }
    }

    override suspend fun getTotalAmountByCategoryAndAccountsInRange(
        categoryId: Int,
        accountsIds: List<Int>,
        dateRange: LongDateRange
    ): Double {
        return records
            .filter {
                it.categoryId == categoryId &&
                        accountsIds.contains(it.accountId) &&
                        dateRange.containsDate(date = it.date)
            }
            .sumOf { it.amount }
    }

}