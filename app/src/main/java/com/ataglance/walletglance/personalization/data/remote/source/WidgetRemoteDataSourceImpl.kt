package com.ataglance.walletglance.personalization.data.remote.source

import com.ataglance.walletglance.personalization.data.remote.model.WidgetDto

class WidgetRemoteDataSourceImpl() : WidgetRemoteDataSource {

    override suspend fun getUpdateTime(userId: Int): Long? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun synchronizeWidgets(
        widgets: List<WidgetDto>,
        timestamp: Long,
        userId: Int
    ): List<WidgetDto>? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun synchronizeWidgetsAndGetAfterTimestamp(
        widgets: List<WidgetDto>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<WidgetDto>? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun getWidgetsAfterTimestamp(timestamp: Long, userId: Int): List<WidgetDto>? {
        // TODO("Not yet implemented")
        return null
    }

}