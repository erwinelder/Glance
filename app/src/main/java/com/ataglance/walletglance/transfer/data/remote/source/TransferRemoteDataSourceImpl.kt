package com.ataglance.walletglance.transfer.data.remote.source

import com.ataglance.walletglance.transfer.data.remote.model.TransferCommandDto
import com.ataglance.walletglance.transfer.data.remote.model.TransferQueryDto

class TransferRemoteDataSourceImpl : TransferRemoteDataSource {

    override suspend fun getUpdateTime(userId: Int): Long? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun synchronizeTransfers(
        transfers: List<TransferCommandDto>,
        timestamp: Long,
        userId: Int
    ): Boolean {
        // TODO("Not yet implemented")
        return false
    }

    override suspend fun synchronizeTransfersAndGetAfterTimestamp(
        transfers: List<TransferCommandDto>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<TransferQueryDto>? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun getTransfersAfterTimestamp(
        timestamp: Long,
        userId: Int
    ): List<TransferQueryDto>? {
        // TODO("Not yet implemented")
        return null
    }

}