package com.ataglance.walletglance.personalization.data.local.source

import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.personalization.data.local.dao.WidgetLocalDao
import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import kotlinx.coroutines.flow.Flow

class WidgetLocalDataSourceImpl(
    private val widgetDao: WidgetLocalDao,
    private val updateTimeDao: LocalUpdateTimeDao
) : WidgetLocalDataSource {

    override suspend fun getUpdateTime(): Long? {
        return updateTimeDao.getUpdateTime(tableName = TableName.Widget.name)
    }

    override suspend fun saveUpdateTime(timestamp: Long) {
        updateTimeDao.saveUpdateTime(tableName = TableName.Widget.name, timestamp = timestamp)
    }

    override suspend fun upsertWidgets(widgets: List<WidgetEntity>, timestamp: Long) {
        widgetDao.upsertEntities(entities = widgets)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun deleteAllWidgets(timestamp: Long) {
        widgetDao.deleteAllWidgets()
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun synchroniseWidgets(
        widgetsToSync: EntitiesToSync<WidgetEntity>,
        timestamp: Long
    ) {
        widgetDao.deleteAndUpsertEntities(
            toDelete = widgetsToSync.toDelete,
            toUpsert = widgetsToSync.toUpsert
        )
        saveUpdateTime(timestamp = timestamp)
    }

    override fun getAllWidgets(): Flow<List<WidgetEntity>> {
        return widgetDao.getAllWidgets()
    }

}

fun getWidgetLocalDataSource(appDatabase: AppDatabase): WidgetLocalDataSource {
    return WidgetLocalDataSourceImpl(
        widgetDao = appDatabase.widgetDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}