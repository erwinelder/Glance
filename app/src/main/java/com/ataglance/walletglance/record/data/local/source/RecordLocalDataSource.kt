package com.ataglance.walletglance.record.data.local.source

import com.ataglance.walletglance.record.data.local.model.RecordEntityWithItems
import kotlinx.coroutines.flow.Flow

interface RecordLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun deleteUpdateTime()

    suspend fun saveRecordsWithItems(
        recordsWithItems: List<RecordEntityWithItems>,
        timestamp: Long
    ): List<RecordEntityWithItems>

    suspend fun deleteRecordsWithItems(
        recordsWithItems: List<RecordEntityWithItems>,
        timestamp: Long? = null
    )

    suspend fun deleteAndSaveRecordsWithItems(
        toDelete: List<RecordEntityWithItems>,
        toUpsert: List<RecordEntityWithItems>,
        timestamp: Long
    ): List<RecordEntityWithItems>

    suspend fun getRecordsWithItemsAfterTimestamp(timestamp: Long): List<RecordEntityWithItems>

    suspend fun getRecordWithItems(id: Long): RecordEntityWithItems?

    suspend fun getLastRecordWithItemsByTypeAndAccount(
        type: Char,
        accountId: Int
    ): RecordEntityWithItems?

    fun getRecordsWithItemsInDateRangeAsFlow(
        from: Long,
        to: Long
    ): Flow<List<RecordEntityWithItems>>

    suspend fun getRecordsWithItemsInDateRange(
        from: Long,
        to: Long
    ): List<RecordEntityWithItems>

    suspend fun getTotalExpensesInDateRangeByAccountsAndCategory(
        from: Long,
        to: Long,
        accountIds: List<Int>,
        categoryId: Int
    ): Double

}