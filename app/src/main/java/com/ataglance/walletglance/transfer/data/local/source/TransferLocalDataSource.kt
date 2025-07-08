package com.ataglance.walletglance.transfer.data.local.source

import com.ataglance.walletglance.transfer.data.local.model.TransferEntity
import kotlinx.coroutines.flow.Flow

interface TransferLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun deleteUpdateTime()

    suspend fun saveTransfers(
        transfers: List<TransferEntity>,
        timestamp: Long
    ): List<TransferEntity>

    suspend fun deleteTransfers(transfers: List<TransferEntity>, timestamp: Long? = null)

    suspend fun deleteAndSaveTransfers(
        toDelete: List<TransferEntity>,
        toUpsert: List<TransferEntity>,
        timestamp: Long
    ): List<TransferEntity>

    suspend fun getTransfersAfterTimestamp(timestamp: Long): List<TransferEntity>

    suspend fun getTransfer(id: Long): TransferEntity?

    fun getTransfersInDateRangeAsFlow(from: Long, to: Long): Flow<List<TransferEntity>>

    suspend fun getTransfersInDateRange(from: Long, to: Long): List<TransferEntity>

    suspend fun getTransfersByAccounts(accountIds: List<Int>): List<TransferEntity>

    suspend fun getTotalExpensesInDateRangeByAccounts(
        from: Long,
        to: Long,
        accountIds: List<Int>
    ): Double

}