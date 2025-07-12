package com.ataglance.walletglance.record.data.remote.source

import com.ataglance.walletglance.record.data.remote.model.RecordCommandDtoWithItems
import com.ataglance.walletglance.record.data.remote.model.RecordQueryDtoWithItems

class RecordRemoteDataSourceImpl : RecordRemoteDataSource {

    override suspend fun getUpdateTime(userId: Int): Long? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun synchronizeRecordsWithItems(
        recordsWithItems: List<RecordCommandDtoWithItems>,
        timestamp: Long,
        userId: Int
    ): Boolean {
        // TODO("Not yet implemented")
        return false
    }

    override suspend fun synchronizeRecordsWithItemsAndGetAfterTimestamp(
        recordsWithItems: List<RecordCommandDtoWithItems>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<RecordQueryDtoWithItems>? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun getRecordsWithItemsAfterTimestamp(
        timestamp: Long,
        userId: Int
    ): List<RecordQueryDtoWithItems>? {
        // TODO("Not yet implemented")
        return null
    }

}