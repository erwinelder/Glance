package com.ataglance.walletglance.transfer.data.remote.source

import com.ataglance.walletglance.transfer.data.remote.model.TransferCommandDto
import com.ataglance.walletglance.transfer.data.remote.model.TransferQueryDto

interface TransferRemoteDataSource {

    suspend fun getUpdateTime(userId: Int): Long?

    suspend fun synchronizeTransfers(
        transfers: List<TransferCommandDto>,
        timestamp: Long,
        userId: Int
    ): List<TransferQueryDto>?

    suspend fun synchronizeTransfersAndGetAfterTimestamp(
        transfers: List<TransferCommandDto>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<TransferQueryDto>?

    suspend fun getTransfersAfterTimestamp(timestamp: Long, userId: Int): List<TransferQueryDto>?

}