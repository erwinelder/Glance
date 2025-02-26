package com.ataglance.walletglance.personalization.data.local.source

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import kotlinx.coroutines.flow.Flow

interface WidgetLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun upsertWidgets(widgets: List<WidgetEntity>, timestamp: Long)

    suspend fun deleteAllWidgets(timestamp: Long)

    suspend fun synchroniseWidgets(widgetsToSync: EntitiesToSync<WidgetEntity>, timestamp: Long)

    fun getAllWidgets(): Flow<List<WidgetEntity>>

}