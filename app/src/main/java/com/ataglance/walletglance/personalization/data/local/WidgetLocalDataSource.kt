package com.ataglance.walletglance.personalization.data.local

import androidx.room.Transaction
import com.ataglance.walletglance.core.data.local.source.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.dao.LocalUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.personalization.data.model.WidgetEntity

class WidgetLocalDataSource(
    private val widgetDao: WidgetDao,
    updateTimeDao: LocalUpdateTimeDao
) : BaseLocalDataSource<WidgetEntity>(
    dao = widgetDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.Widget
) {

    @Transaction
    suspend fun deleteAllWidgets(timestamp: Long) {
        widgetDao.deleteAllWidgets()
        updateLastModifiedTime(timestamp)
    }

}