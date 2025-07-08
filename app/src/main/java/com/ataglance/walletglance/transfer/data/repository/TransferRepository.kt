package com.ataglance.walletglance.transfer.data.repository

import com.ataglance.walletglance.transfer.data.model.TransferDataModel
import kotlinx.coroutines.flow.Flow

interface TransferRepository {

    suspend fun upsertTransfer(transfer: TransferDataModel)

    suspend fun deleteTransfer(transfer: TransferDataModel)

    suspend fun getTransfer(id: Long): TransferDataModel?

    fun getTransfersInDateRangeAsFlow(from: Long, to: Long): Flow<List<TransferDataModel>>

    suspend fun getTransfersInDateRange(from: Long, to: Long): List<TransferDataModel>

    suspend fun getTransfersByAccounts(ids: List<Int>): List<TransferDataModel>

    suspend fun getTotalExpensesInDateRangeByAccounts(
        from: Long,
        to: Long,
        accountIds: List<Int>
    ): Double

}