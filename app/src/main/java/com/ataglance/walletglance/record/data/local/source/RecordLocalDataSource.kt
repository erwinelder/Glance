package com.ataglance.walletglance.record.data.local.source

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import kotlinx.coroutines.flow.Flow

interface RecordLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun upsertRecords(records: List<RecordEntity>, timestamp: Long): List<RecordEntity>

    suspend fun deleteRecords(records: List<RecordEntity>, timestamp: Long)

    suspend fun deleteAllRecords(timestamp: Long)

    suspend fun synchroniseRecords(
        recordsToSync: EntitiesToSync<RecordEntity>,
        timestamp: Long
    ): List<RecordEntity>

    suspend fun convertTransfersToRecords(noteValues: List<String>, timestamp: Long)

    suspend fun getLastRecordNum(): Flow<Int?>

    suspend fun getLastRecordsByTypeAndAccount(type: Char, accountId: Int): List<RecordEntity>

    suspend fun getRecordsByRecordNum(recordNum: Int): List<RecordEntity>

    suspend fun getRecordsInDateRange(range: LongDateRange): Flow<List<RecordEntity>>

    suspend fun getTotalAmountByCategoryAndAccountsInRange(
        categoryId: Int,
        linkedAccountsIds: List<Int>,
        longDateRange: LongDateRange
    ): Double

}