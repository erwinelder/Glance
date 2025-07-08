package com.ataglance.walletglance.personalization.data.local.source

import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.local.database.AppDatabase
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.personalization.data.local.dao.WidgetLocalDao
import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

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

    override suspend fun deleteUpdateTime() {
        updateTimeDao.deleteUpdateTime(tableName = TableName.Widget.name)
    }

    override suspend fun upsertWidgets(widgets: List<WidgetEntity>, timestamp: Long) {
        widgetDao.upsertWidgets(widgets = widgets)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun deleteWidgets(widgets: List<WidgetEntity>) {
        widgetDao.deleteWidgets(widgets = widgets)
    }

    override suspend fun deleteAllWidgets() {
        widgetDao.deleteAllWidgets()
        deleteUpdateTime()
    }

    override suspend fun deleteAndUpsertWidgets(
        toDelete: List<WidgetEntity>,
        toUpsert: List<WidgetEntity>,
        timestamp: Long
    ) {
        widgetDao.deleteAndUpsertWidgets(toDelete = toDelete, toUpsert = toUpsert)
        saveUpdateTime(timestamp = timestamp)
    }

    override suspend fun getWidgetsAfterTimestamp(timestamp: Long): List<WidgetEntity> {
        return widgetDao.getWidgetsAfterTimestamp(timestamp = timestamp)
    }

    override fun getAllWidgetsAsFlow(): Flow<List<WidgetEntity>> {
        return widgetDao.getAllWidgetsAsFlow()
    }

    override suspend fun getAllWidgets(): List<WidgetEntity> {
        return widgetDao.getAllWidgetsAsFlow().firstOrNull().orEmpty()
    }

}

fun getWidgetLocalDataSource(appDatabase: AppDatabase): WidgetLocalDataSource {
    return WidgetLocalDataSourceImpl(
        widgetDao = appDatabase.widgetDao,
        updateTimeDao = appDatabase.localUpdateTimeDao
    )
}