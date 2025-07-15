package com.ataglance.walletglance.personalization.data.remote.source

import com.ataglance.walletglance.personalization.data.remote.model.WidgetDto

interface WidgetRemoteDataSource {

    suspend fun getUpdateTime(userId: Int): Long?

    suspend fun synchronizeWidgets(
        widgets: List<WidgetDto>,
        timestamp: Long,
        userId: Int
    ): Boolean

    suspend fun synchronizeWidgetsAndGetAfterTimestamp(
        widgets: List<WidgetDto>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<WidgetDto>?

    suspend fun getWidgetsAfterTimestamp(timestamp: Long, userId: Int): List<WidgetDto>?

}