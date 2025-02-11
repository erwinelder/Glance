package com.ataglance.walletglance.personalization.data.remote.source

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.personalization.data.remote.model.WidgetRemoteEntity

interface WidgetRemoteDataSource {

    suspend fun getUpdateTime(userId: String): Long?

    suspend fun saveUpdateTime(timestamp: Long, userId: String)

    suspend fun upsertWidgets(widgets: List<WidgetRemoteEntity>, timestamp: Long, userId: String)

    suspend fun synchroniseWidgets(
        widgetsToSync: EntitiesToSync<WidgetRemoteEntity>,
        timestamp: Long,
        userId: String
    )

    suspend fun getWidgetsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<WidgetRemoteEntity>

}