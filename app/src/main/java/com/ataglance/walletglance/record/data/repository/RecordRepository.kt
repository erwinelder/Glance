package com.ataglance.walletglance.record.data.repository

import com.ataglance.walletglance.core.domain.date.LongDateRange
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

    suspend fun getLastRecordsByTypeAndAccount(type: Char, accountId: Int): List<RecordEntity>

    @Deprecated("Use getRecordsInDateRange instead")
    fun getRecordsForToday(): Flow<List<RecordEntity>>

    suspend fun getRecordsByRecordNum(recordNum: Int): List<RecordEntity>

    fun getRecordsInDateRange(range: LongDateRange): Flow<List<RecordEntity>>

    suspend fun getTotalAmountByCategoryAndAccountsInRange(
        categoryId: Int,
        accountsIds: List<Int>,
        dateRange: LongDateRange
    ): Double

}