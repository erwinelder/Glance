package com.ataglance.walletglance.transfer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.transfer.data.local.model.TransferEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransferLocalDao {

    @Delete
    suspend fun deleteTransfers(transfers: List<TransferEntity>)

    @Insert
    suspend fun insertTransfers(transfers: List<TransferEntity>): List<Long>

    @Upsert
    suspend fun upsertTransfers(transfers: List<TransferEntity>)

    @Transaction
    suspend fun saveTransfers(transfers: List<TransferEntity>): List<TransferEntity> {
        val (toInsert, toUpsert) = transfers.partition { it.id == 0L }
        upsertTransfers(transfers = toUpsert)
        val insertedIds = insertTransfers(transfers = toInsert).toMutableList()

        return toUpsert + toInsert.mapNotNull { transfer ->
            insertedIds.removeFirstOrNull()?.let { id ->
                transfer.copy(id = id)
            }
        }
    }

    @Transaction
    suspend fun deleteAndSaveTransfers(
        toDelete: List<TransferEntity>,
        toUpsert: List<TransferEntity>
    ): List<TransferEntity> {
        deleteTransfers(transfers = toDelete)
        return saveTransfers(transfers = toUpsert)
    }

    @Query("SELECT * FROM transfer WHERE timestamp > :timestamp ORDER BY date DESC")
    suspend fun getTransfersAfterTimestamp(timestamp: Long): List<TransferEntity>

    @Query("SELECT * FROM transfer WHERE id = :id")
    suspend fun getTransfer(id: Long): TransferEntity?

    @Query("SELECT * FROM transfer WHERE date BETWEEN :from AND :to ORDER BY date DESC")
    fun getTransfersInDateRangeAsFlow(
        from: Long,
        to: Long
    ): Flow<List<TransferEntity>>

    @Query("SELECT * FROM transfer WHERE date BETWEEN :from AND :to ORDER BY date DESC")
    suspend fun getTransfersInDateRange(from: Long, to: Long): List<TransferEntity>

    @Query("""
        SELECT * FROM transfer
        WHERE senderAccountId IN (:accountIds) OR receiverAccountId IN (:accountIds)
        ORDER BY date DESC
    """)
    suspend fun getTransfersByAccounts(accountIds: List<Int>): List<TransferEntity>

    @Query("""
        SELECT SUM(senderAmount) FROM transfer
        WHERE date BETWEEN :from AND :to AND senderAccountId IN (:accountIds)
    """)
    suspend fun getTotalExpensesInDateRangeByAccounts(
        from: Long,
        to: Long,
        accountIds: List<Int>
    ): Double?

}