package com.ataglance.walletglance.personalization.data.local

import com.ataglance.walletglance.core.data.local.BaseLocalDataSource
import com.ataglance.walletglance.core.data.local.TableUpdateTimeDao
import com.ataglance.walletglance.core.data.model.TableName
import com.ataglance.walletglance.personalization.data.model.WidgetEntity
import kotlinx.coroutines.flow.Flow

class WidgetLocalDataSource(
    private val widgetDao: WidgetDao,
    updateTimeDao: TableUpdateTimeDao
) : BaseLocalDataSource<WidgetEntity>(
    dao = widgetDao,
    updateTimeDao = updateTimeDao,
    tableName = TableName.Widget
) {

    fun getAllWidgets(): Flow<List<WidgetEntity>> = widgetDao.getAllWidgets()

}