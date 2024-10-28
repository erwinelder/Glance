package com.ataglance.walletglance.personalization.data.local

import androidx.room.Transaction
import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.TableUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.personalization.data.model.WidgetEntity

class WidgetLocalDataSource(
    private val widgetDao: WidgetDao,
    updateTimeDao: TableUpdateTimeDao
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