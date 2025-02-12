package com.ataglance.walletglance.record.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordLocalDao {

    @Upsert
    suspend fun upsertRecords(records: List<RecordEntity>)

    @Delete
    suspend fun deleteRecords(records: List<RecordEntity>)

    @Transaction
    suspend fun deleteAndUpsertRecords(toDelete: List<RecordEntity>, toUpsert: List<RecordEntity>) {
        deleteRecords(toDelete)
        upsertRecords(toUpsert)
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

    @Query("SELECT * FROM Record WHERE recordNum = :recordNum")
    fun getRecordsByRecordNum(recordNum: Int): List<RecordEntity>

    @Query("""    
        SELECT * FROM Record
        WHERE date BETWEEN :startPastDate AND :endFutureDate
        ORDER BY date DESC
    """)
    fun getRecordsInDateRange(startPastDate: Long, endFutureDate: Long): Flow<List<RecordEntity>>

    @Query("""
        SELECT SUM(amount) FROM Record
        WHERE includeInBudgets = 1
            AND accountId IN (:linkedAccountsIds)
            AND (categoryId = :categoryId OR subcategoryId = :categoryId)
            AND date BETWEEN :from AND :to
    """)
    suspend fun getTotalAmountForBudgetInDateRange(
        linkedAccountsIds: List<Int>,
        categoryId: Int,
        from: Long,
        to: Long
    ): Double?

}