package com.ataglance.walletglance.personalization.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.core.data.local.BaseDao
import com.ataglance.walletglance.personalization.data.model.WidgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WidgetDao : BaseDao<WidgetEntity> {

    @Query("SELECT * FROM Widget")
    fun getAllWidgets(): Flow<List<WidgetEntity>>

}