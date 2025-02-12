package com.ataglance.walletglance.personalization.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WidgetLocalDao {

    @Upsert
    suspend fun upsertWidgets(widgets: List<WidgetEntity>)

    @Delete
    suspend fun deleteWidgets(widgets: List<WidgetEntity>)

    @Transaction
    suspend fun deleteAndUpsertWidgets(
        toDelete: List<WidgetEntity>,
        toUpsert: List<WidgetEntity>
    ) {
        deleteWidgets(toDelete)
        upsertWidgets(toUpsert)
    }

    @Query("DELETE FROM Widget")
    suspend fun deleteAllWidgets()

    @Query("SELECT * FROM Widget")
    fun getAllWidgets(): Flow<List<WidgetEntity>>

}