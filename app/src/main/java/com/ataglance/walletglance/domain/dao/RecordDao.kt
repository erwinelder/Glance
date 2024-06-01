package com.ataglance.walletglance.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ataglance.walletglance.domain.entities.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceRecords(recordList: List<Record>)

    @Delete
    suspend fun deleteRecords(recordList: List<Record>)

    @Query("DELETE FROM Record WHERE accountId == :id")
    suspend fun deleteRecordsByAccountId(id: Int)

    @Query("DELETE FROM Record WHERE recordNum IN (:recordNumbers)")
    suspend fun deleteRecordsByRecordNumbers(recordNumbers: List<Int>)

    @Query("DELETE FROM Record")
    suspend fun deleteAllRecords()

    @Query("SELECT recordNum FROM Record ORDER BY recordNum DESC LIMIT 1")
    fun getLastRecordOrderNum(): Flow<Int?>

    @Query("SELECT * FROM Record")
    fun getAllRecords(): Flow<List<Record>>

    @Query("SELECT * FROM Record WHERE (date BETWEEN :startPastDate AND :endFutureDate) OR (date BETWEEN :todayStartPast AND :todayEndFuture) ORDER BY date DESC")
    fun getRecordsInDateRange(
        todayStartPast: Long, todayEndFuture: Long,
        startPastDate: Long, endFutureDate: Long
    ): Flow<List<Record>>

    @Query("SELECT * FROM Record WHERE (accountId == :accountId) AND (type == 60 OR type == 62)")
    fun getTransfersByAccountId(accountId: Int): Flow<List<Record>>

    @Query("SELECT * FROM Record WHERE recordNum IN (:recordNumbers)")
    fun getRecordsByRecordNumbers(recordNumbers: List<Int>): Flow<List<Record>>
}