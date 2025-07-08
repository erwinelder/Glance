package com.ataglance.walletglance.record.data.remote.source

import com.ataglance.walletglance.record.data.remote.model.RecordCommandDtoWithItems
import com.ataglance.walletglance.record.data.remote.model.RecordQueryDtoWithItems

interface RecordRemoteDataSource {

    suspend fun getUpdateTime(userId: Int): Long?

    suspend fun synchronizeRecordsWithItems(
        recordsWithItems: List<RecordCommandDtoWithItems>,
        timestamp: Long,
        userId: Int
    ): List<RecordQueryDtoWithItems>?

    suspend fun synchronizeRecordsWithItemsAndGetAfterTimestamp(
        recordsWithItems: List<RecordCommandDtoWithItems>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<RecordQueryDtoWithItems>?

    suspend fun getRecordsWithItemsAfterTimestamp(
        timestamp: Long,
        userId: Int
    ): List<RecordQueryDtoWithItems>?

}