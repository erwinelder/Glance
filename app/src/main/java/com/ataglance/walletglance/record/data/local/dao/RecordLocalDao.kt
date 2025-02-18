package com.ataglance.walletglance.record.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordLocalDao {

    suspend fun upsertRecords(records: List<RecordEntity>): List<RecordEntity> {
        val (toInsert, toUpdate) = records.partition { it.id == 0 }
        upsertUpdateRecords(toUpdate)
        val insertedIds = insertRecords(toInsert).toMutableList()

        return records.mapIndexed { index, record ->
            record.takeIf { it.id != 0 } ?: record.copy(id = insertedIds[index].toInt())
        }
    }

    @Upsert
    suspend fun upsertUpdateRecords(toUpsert: List<RecordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecords(records: List<RecordEntity>): List<Long>

    @Delete
    suspend fun deleteRecords(records: List<RecordEntity>)

    @Transaction
    suspend fun deleteAndUpsertRecords(
        toDelete: List<RecordEntity>,
        toUpsert: List<RecordEntity>
    ): List<RecordEntity> {
        deleteRecords(toDelete)
        return upsertRecords(toUpsert)
    }

    @Query("DELETE FROM Record")
    suspend fun deleteAllRecords()

    @Query(
        """
        UPDATE Record
        SET type = CASE WHEN type = 62 THEN 45 ELSE 43 END,
            categoryId = CASE WHEN type = 62 THEN 12 ELSE 77 END,
            subcategoryId = CASE WHEN type = 62 THEN 66 ELSE NULL END,
            note = NULL
        WHERE note IN (:noteValues)
    """
    )
    suspend fun convertTransfersToRecords(noteValues: List<String>)

    @Query("SELECT recordNum FROM Record ORDER BY recordNum DESC LIMIT 1")
    fun getLastRecordOrderNum(): Flow<Int?>

    @Query("""
        SELECT r.*
        FROM Record r
        INNER JOIN (
            SELECT recordNum
            FROM Record
            WHERE type = :type
            ORDER BY date DESC
            LIMIT 1
        ) AS latest
        ON r.recordNum = latest.recordNum
    """)
    suspend fun getLastRecordsByType(type: Char): List<RecordEntity>

    @Query("""
        SELECT r.*
        FROM Record r
        INNER JOIN (
            SELECT recordNum
            FROM Record
            WHERE type = :type AND accountId = :accountId
            ORDER BY date DESC
            LIMIT 1
        ) AS latest
        ON r.recordNum = latest.recordNum
    """)
    suspend fun getLastRecordsByTypeAndAccount(type: Char, accountId: Int): List<RecordEntity>

    @Query("SELECT * FROM Record WHERE recordNum = :recordNum")
    suspend fun getRecordsByRecordNum(recordNum: Int): List<RecordEntity>

    @Query("""    
        SELECT * FROM Record
        WHERE date BETWEEN :startPastDate AND :endFutureDate
        ORDER BY date DESC
    """)
    fun getRecordsInDateRange(startPastDate: Long, endFutureDate: Long): Flow<List<RecordEntity>>

    @Query("""
        SELECT SUM(amount) FROM Record
        WHERE includeInBudgets = 1
            AND (categoryId = :categoryId OR subcategoryId = :categoryId)
            AND accountId IN (:linkedAccountsIds)
            AND date BETWEEN :from AND :to
    """)
    suspend fun getTotalAmountByCategoryAndAccountsInRange(
        categoryId: Int,
        linkedAccountsIds: List<Int>,
        from: Long,
        to: Long
    ): Double?

}