package com.ataglance.walletglance.personalization.data.local.source

import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import kotlinx.coroutines.flow.Flow

interface WidgetLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun deleteUpdateTime()

    suspend fun upsertWidgets(widgets: List<WidgetEntity>, timestamp: Long)

    suspend fun deleteWidgets(widgets: List<WidgetEntity>)

    suspend fun deleteAllWidgets()


    suspend fun deleteAndUpsertWidgets(
        toDelete: List<WidgetEntity>,
        toUpsert: List<WidgetEntity>,
        timestamp: Long
    )

    suspend fun getWidgetsAfterTimestamp(timestamp: Long): List<WidgetEntity>

    fun getAllWidgetsAsFlow(): Flow<List<WidgetEntity>>

    suspend fun getAllWidgets(): List<WidgetEntity>

}