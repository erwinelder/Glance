package com.ataglance.walletglance.personalization.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.core.data.local.dao.BaseLocalDao
import com.ataglance.walletglance.personalization.data.local.model.WidgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WidgetLocalDao : BaseLocalDao<WidgetEntity> {

    @Query("DELETE FROM Widget")
    suspend fun deleteAllWidgets()

    @Query("SELECT * FROM Widget")
    fun getAllWidgets(): Flow<List<WidgetEntity>>

}