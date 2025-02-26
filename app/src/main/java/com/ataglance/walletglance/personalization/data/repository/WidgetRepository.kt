package com.ataglance.walletglance.personalization.data.repository

import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import kotlinx.coroutines.flow.Flow

interface WidgetRepository {

    suspend fun upsertWidgets(widgets: List<WidgetEntity>)

    suspend fun deleteAndUpsertWidgets(toDelete: List<WidgetEntity>, toUpsert: List<WidgetEntity>)

    suspend fun deleteAllWidgetsLocally()

    fun getAllWidgetsFlow(): Flow<List<WidgetEntity>>

    suspend fun getAllWidgets(): List<WidgetEntity>

}