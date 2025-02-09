package com.ataglance.walletglance.record.data.repository

import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.TotalAmountByRange
import com.ataglance.walletglance.core.data.model.LongDateRange
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import kotlinx.coroutines.flow.Flow

interface RecordRepository {

    suspend fun upsertRecords(records: List<RecordEntity>)

    suspend fun deleteRecords(records: List<RecordEntity>)

    suspend fun deleteAndUpsertRecords(
        toDelete: List<RecordEntity>,
        toUpsert: List<RecordEntity>
    )

    suspend fun deleteAllRecordsLocally()

    suspend fun convertRecordsToTransfers(noteValues: List<String>)

    fun getLastRecordNum(): Flow<Int?>

    @Deprecated("Use getRecordsInDateRange instead")
    fun getRecordsForToday(): Flow<List<RecordEntity>>

    suspend fun getRecordsByRecordNum(recordNum: Int): List<RecordEntity>

    fun getRecordsInDateRange(range: LongDateRange): Flow<List<RecordEntity>>

    fun getTotalAmountForBudgetInDateRanges(
        budget: Budget,
        dateRangeList: List<LongDateRange>
    ): Flow<List<TotalAmountByRange>>

}