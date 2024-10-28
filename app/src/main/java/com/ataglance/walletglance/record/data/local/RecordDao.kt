package com.ataglance.walletglance.record.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.core.data.local.BaseDao
import com.ataglance.walletglance.record.data.model.RecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao : BaseDao<RecordEntity> {

    @Query("DELETE FROM Record WHERE recordNum IN (:recordNumbers)")
    suspend fun deleteRecordsByRecordNumbers(recordNumbers: List<Int>)

    @Query("DELETE FROM Record")
    suspend fun deleteAllRecords()

    @Query("SELECT recordNum FROM Record ORDER BY recordNum DESC LIMIT 1")
    fun getLastRecordOrderNum(): Flow<Int?>

    @Query("SELECT * FROM Record WHERE recordNum IN (:recordNumbers)")
    fun getRecordsByRecordNumbers(recordNumbers: List<Int>): Flow<List<RecordEntity>>

    @Query("""    
        SELECT * FROM Record
        WHERE date BETWEEN :startPastDate AND :endFutureDate
        ORDER BY date DESC
    """)
    fun getRecordsInDateRange(startPastDate: Long, endFutureDate: Long): Flow<List<RecordEntity>>

    @Query("SELECT * FROM Record")
    override fun getAllEntities(): Flow<List<RecordEntity>>

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
    fun getTransfersByAccountId(accountId: Int): Flow<List<RecordEntity>>

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