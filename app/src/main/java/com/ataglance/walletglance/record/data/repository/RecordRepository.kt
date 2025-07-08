package com.ataglance.walletglance.record.data.repository

import com.ataglance.walletglance.core.domain.date.TimestampRange
import com.ataglance.walletglance.record.data.model.RecordDataModelWithItems
import kotlinx.coroutines.flow.Flow

interface RecordRepository {

    suspend fun upsertRecordWithItems(recordWithItems: RecordDataModelWithItems)

    suspend fun upsertRecordsWithItems(recordsWithItems: List<RecordDataModelWithItems>)

    suspend fun deleteRecordWithItems(recordWithItems: RecordDataModelWithItems)

    suspend fun deleteAndUpsertRecordWithItems(
        recordWithItemsToDelete: RecordDataModelWithItems,
        recordWithItemsToUpsert: RecordDataModelWithItems
    )

    suspend fun getRecordWithItems(id: Long): RecordDataModelWithItems?

    suspend fun getLastRecordWithItemsByTypeAndAccount(
        type: Char,
        accountId: Int
    ): RecordDataModelWithItems?

    fun getRecordsWithItemsInDateRangeAsFlow(
        from: Long,
        to: Long
    ): Flow<List<RecordDataModelWithItems>>

    suspend fun getRecordsWithItemsInDateRange(from: Long, to: Long): List<RecordDataModelWithItems>

    suspend fun getTotalExpensesInDateRangeByAccountsAndCategory(
        dateRange: TimestampRange,
        accountIds: List<Int>,
        categoryId: Int
    ): Double

}