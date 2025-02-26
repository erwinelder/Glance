package com.ataglance.walletglance.personalization.data.remote.source

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.core.data.remote.dao.RemoteUpdateTimeDao
import com.ataglance.walletglance.personalization.data.remote.dao.WidgetRemoteDao
import com.ataglance.walletglance.personalization.data.remote.model.WidgetRemoteEntity

class WidgetRemoteDataSourceImpl(
    private val widgetDao: WidgetRemoteDao,
    private val updateTimeDao: RemoteUpdateTimeDao
) : WidgetRemoteDataSource {

    override suspend fun getUpdateTime(userId: String): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.Widget.name, userId = userId)
    }

    override suspend fun saveUpdateTime(timestamp: Long, userId: String) {
        updateTimeDao.saveUpdateTime(
            tableName = TableName.Widget.name, timestamp = timestamp, userId = userId
        )
    }

    override suspend fun upsertWidgets(
        widgets: List<WidgetRemoteEntity>,
        timestamp: Long,
        userId: String
    ) {
        widgetDao.upsertWidgets(widgets = widgets, userId = userId)
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun synchroniseWidgets(
        widgetsToSync: EntitiesToSync<WidgetRemoteEntity>,
        timestamp: Long,
        userId: String
    ) {
        widgetDao.synchroniseWidgets(widgetsToSync = widgetsToSync, userId = userId)
        saveUpdateTime(timestamp = timestamp, userId = userId)
    }

    override suspend fun getWidgetsAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<WidgetRemoteEntity> {
        return widgetDao.getWidgetsAfterTimestamp(timestamp = timestamp, userId = userId)
    }

}