package com.ataglance.walletglance.personalization.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.core.data.local.BaseDao
import com.ataglance.walletglance.personalization.data.model.WidgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WidgetDao : BaseDao<WidgetEntity> {

    @Query("DELETE FROM Widget")
    suspend fun deleteAllWidgets()

    @Query("SELECT * FROM Widget ORDER BY orderNum ASC")
    override fun getAllEntities(): Flow<List<WidgetEntity>>

}