package com.ataglance.walletglance.personalization.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.personalization.data.local.dao.WidgetDao
import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import kotlinx.coroutines.flow.Flow

class WidgetRepository(
    private val dao: WidgetDao
) {

    suspend fun upsertWidgets(widgetList: List<WidgetEntity>) {
        dao.upsertWidgets(widgetList)
    }

    @Transaction
    suspend fun deleteAndUpsertWidgets(
        widgetListToDelete: List<WidgetEntity>,
        widgetListToUpsert: List<WidgetEntity>,
    ) {
        if (widgetListToDelete.isNotEmpty()) {
            dao.deleteWidgets(widgetListToDelete)
        }
        if (widgetListToUpsert.isNotEmpty()) {
            dao.upsertWidgets(widgetListToUpsert)
        }
    }

    fun getAllWidgets(): Flow<List<WidgetEntity>> {
        return dao.getAllWidgets()
    }

}