package com.ataglance.walletglance.record.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.data.local.model.RecordEntityWithItems
import com.ataglance.walletglance.record.data.local.model.RecordItemEntity
import com.ataglance.walletglance.record.data.utils.zipWithItems
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@Dao
interface RecordLocalDao {

    @Delete
    suspend fun deleteRecords(records: List<RecordEntity>)

    @Insert
    suspend fun insertRecords(records: List<RecordEntity>): List<Long>

    @Upsert
    suspend fun upsertRecords(records: List<RecordEntity>)

    @Transaction
    suspend fun saveRecords(records: List<RecordEntity>): List<Long> {
        val (toInsert, toUpsert) = records.partition { it.id == 0L }
        upsertRecords(records = toUpsert)
        return insertRecords(records = toInsert)
    }

    @Query("SELECT * FROM record WHERE timestamp > :timestamp ORDER BY date DESC")
    suspend fun getRecordsAfterTimestamp(timestamp: Long): List<RecordEntity>

    @Query("SELECT * FROM record WHERE id = :id")
    suspend fun getRecordById(id: Long): RecordEntity?

    @Query("""
        SELECT * FROM record WHERE type = :type AND accountId = :accountId
        ORDER BY date DESC LIMIT 1
    """)
    suspend fun getLastRecordByTypeAndAccount(type: Char, accountId: Int): RecordEntity?

    @Query("SELECT * FROM record WHERE date BETWEEN :from AND :to ORDER BY date DESC")
    fun getRecordsInDateRangeAsFlow(from: Long, to: Long): Flow<List<RecordEntity>>

    @Query("SELECT * FROM record WHERE date BETWEEN :from AND :to ORDER BY date DESC")
    suspend fun getRecordsInDateRange(from: Long, to: Long): List<RecordEntity>

    @Query("""
        SELECT * FROM record
        WHERE date BETWEEN :from AND :to AND accountId IN (:accountIds)
        ORDER BY date DESC
    """)
    suspend fun getRecordsInDateRangeByAccounts(
        from: Long,
        to: Long,
        accountIds: List<Int>,
    ): List<RecordEntity>


    @Insert
    suspend fun insertRecordItems(items: List<RecordItemEntity>): List<Long>

    @Upsert
    suspend fun upsertRecordItems(items: List<RecordItemEntity>)

    @Transaction
    suspend fun saveRecordItems(items: List<RecordItemEntity>): List<Long> {
        val (toInsert, toUpsert) = items.partition { it.id == 0L }
        upsertRecordItems(items = toUpsert)
        return insertRecordItems(items = toInsert)
    }

    @Query("SELECT * FROM record_item WHERE recordId = :id")
    suspend fun getRecordItemsByRecordId(id: Long): List<RecordItemEntity>

    @Query("SELECT * FROM record_item WHERE recordId IN (:ids)")
    fun getRecordItemsByRecordIdsAsFlow(ids: List<Long>): Flow<List<RecordItemEntity>>

    @Query("SELECT * FROM record_item WHERE recordId IN (:ids)")
    suspend fun getRecordItemsByRecordIds(ids: List<Long>): List<RecordItemEntity>

    @Query("""
        SELECT SUM(totalAmount) FROM record_item 
        WHERE recordId IN (:ids) 
        AND (categoryId = :categoryId OR subcategoryId = :categoryId)
    """)
    suspend fun getTotalExpensesByRecordIdsAndCategory(
        ids: List<Long>,
        categoryId: Int
    ): Double?


    suspend fun deleteRecordsWithItems(recordsWithItems: List<RecordEntityWithItems>) {
        deleteRecords(records = recordsWithItems.map { it.record })
    }

    @Transaction
    suspend fun saveRecordsWithItems(
        recordsWithItems: List<RecordEntityWithItems>
    ): List<RecordEntityWithItems> {
        var (recordsWithItemsToInsert, recordsWithItemsToUpsert) = recordsWithItems
            .partition { it.recordId == 0L }
        upsertRecords(records = recordsWithItemsToUpsert.map { it.record })
        val insertedRecordIds = insertRecords(records = recordsWithItemsToInsert.map { it.record })
            .toMutableList()

        recordsWithItemsToInsert = recordsWithItemsToInsert.mapNotNull { recordWithItems ->
            insertedRecordIds.removeFirstOrNull()?.let { id ->
                recordWithItems.replaceRecordId(id = id)
            }
        }

        var (itemsToInsert, itemsToUpsert) = (recordsWithItemsToInsert + recordsWithItemsToUpsert)
            .flatMap { it.items }
            .partition { it.id == 0L }

        upsertRecordItems(items = itemsToUpsert)
        val insertedItemIds = insertRecordItems(items = itemsToInsert).toMutableList()

        itemsToInsert = itemsToInsert.mapNotNull { item ->
            insertedItemIds.removeFirstOrNull()?.let { id ->
                item.copy(id = id)
            }
        }

        return (recordsWithItemsToInsert + recordsWithItemsToUpsert)
            .map { it.record }
            .zipWithItems(items = itemsToInsert + itemsToUpsert)
    }

    @Transaction
    suspend fun deleteAndSaveRecordsWithItems(
        toDelete: List<RecordEntityWithItems>,
        toUpsert: List<RecordEntityWithItems>
    ): List<RecordEntityWithItems> {
        deleteRecords(records = toDelete.map { it.record })
        return saveRecordsWithItems(recordsWithItems = toUpsert)
    }

    suspend fun getRecordsWithItemsAfterTimestamp(timestamp: Long): List<RecordEntityWithItems> {
        val records = getRecordsAfterTimestamp(timestamp = timestamp)
        val items = getRecordItemsByRecordIds(ids = records.map { it.id })

        return records.zipWithItems(items = items)
    }

    suspend fun getRecordWithItems(id: Long): RecordEntityWithItems? {
        val record = getRecordById(id = id) ?: return null
        val items = getRecordItemsByRecordId(id = id)

        return RecordEntityWithItems(record = record, items = items)
    }

    suspend fun getLastRecordWithItemsByTypeAndAccount(
        type: Char,
        accountId: Int
    ): RecordEntityWithItems? {
        val record = getLastRecordByTypeAndAccount(type = type, accountId = accountId)
            ?: return null
        val items = getRecordItemsByRecordId(id = record.id)

        return RecordEntityWithItems(record = record, items = items)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getRecordsWithItemsInDateRangeAsFlow(
        from: Long,
        to: Long
    ): Flow<List<RecordEntityWithItems>> {
        return getRecordsInDateRangeAsFlow(from = from, to = to).flatMapLatest { records ->
            getRecordItemsByRecordIdsAsFlow(ids = records.map { it.id }).map { items ->
                records.zipWithItems(items = items)
            }
        }
    }

    suspend fun getRecordsWithItemsInDateRange(
        from: Long,
        to: Long
    ): List<RecordEntityWithItems> {
        val records = getRecordsInDateRange(from = from, to = to)
        val items = getRecordItemsByRecordIds(ids = records.map { it.id })

        return records.zipWithItems(items = items)
    }

    suspend fun getTotalExpensesInDateRangeByAccountsAndCategory(
        from: Long,
        to: Long,
        accountIds: List<Int>,
        categoryId: Int
    ): Double {
        val records = getRecordsInDateRangeByAccounts(from = from, to = to, accountIds = accountIds)

        return getTotalExpensesByRecordIdsAndCategory(
            ids = records.map { it.id },
            categoryId = categoryId
        ) ?: 0.0
    }

}