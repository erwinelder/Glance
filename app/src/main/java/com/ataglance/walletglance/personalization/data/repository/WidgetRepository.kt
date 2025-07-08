package com.ataglance.walletglance.personalization.data.repository

import com.ataglance.walletglance.personalization.data.model.WidgetDataModel
import kotlinx.coroutines.flow.Flow

interface WidgetRepository {

    suspend fun upsertWidgets(widgets: List<WidgetDataModel>)

    suspend fun deleteAndUpsertWidgets(
        toDelete: List<WidgetDataModel>,
        toUpsert: List<WidgetDataModel>
    )

    suspend fun deleteAllWidgetsLocally()

    fun getAllWidgetsAsFlow(): Flow<List<WidgetDataModel>>

    suspend fun getAllWidgets(): List<WidgetDataModel>

}