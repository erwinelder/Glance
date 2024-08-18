package com.ataglance.walletglance.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ataglance.walletglance.domain.entities.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {

    @Upsert
    suspend fun upsertRecords(recordList: List<Record>)

    @Delete
    suspend fun deleteRecords(recordList: List<Record>)

    @Query("DELETE FROM Record WHERE id IN (:idList)")
    suspend fun deleteRecordsByIds(idList: List<Int>)

    @Query("DELETE FROM Record WHERE recordNum IN (:recordNumbers)")
    suspend fun deleteRecordsByRecordNumbers(recordNumbers: List<Int>)

    @Query("DELETE FROM Record")
    suspend fun deleteAllRecords()

    @Query("SELECT recordNum FROM Record ORDER BY recordNum DESC LIMIT 1")
    fun getLastRecordOrderNum(): Flow<Int?>

    @Query("SELECT * FROM Record")
    fun getAllRecords(): Flow<List<Record>>

    @Query("""    
        SELECT * FROM Record
        WHERE date BETWEEN :startPastDate AND :endFutureDate
        ORDER BY date DESC
    """)
    fun getRecordsInDateRange(startPastDate: Long, endFutureDate: Long): Flow<List<Record>>

    @Query("""
        SELECT SUM(amount) FROM Record
        WHERE includeInBudgets = 1
            AND accountId IN (:linkedAccountsIds)
            AND (categoryId = :categoryId OR subcategoryId = :categoryId)
            AND date BETWEEN :from AND :to
    """)
    fun getTotalAmountForBudgetInDateRange(
        linkedAccountsIds: List<Int>,
        categoryId: Int,
        from: Long,
        to: Long
    ): Flow<Double>

    @Query("SELECT * FROM Record WHERE (accountId == :accountId) AND (type == 60 OR type == 62)")
    fun getTransfersByAccountId(accountId: Int): Flow<List<Record>>

    @Query("SELECT * FROM Record WHERE recordNum IN (:recordNumbers)")
    fun getRecordsByRecordNumbers(recordNumbers: List<Int>): Flow<List<Record>>

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

}