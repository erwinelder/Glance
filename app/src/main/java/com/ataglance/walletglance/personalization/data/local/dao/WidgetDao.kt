package com.ataglance.walletglance.personalization.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WidgetDao {

    @Upsert
    suspend fun upsertWidgets(widgetList: List<WidgetEntity>)

    @Delete
    suspend fun deleteWidgets(widgetList: List<WidgetEntity>)

    @Query("SELECT * FROM Widget")
    fun getAllWidgets(): Flow<List<WidgetEntity>>

}