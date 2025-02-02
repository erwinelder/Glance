package com.ataglance.walletglance.personalization.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.core.data.local.dao.BaseLocalDao
import com.ataglance.walletglance.personalization.data.model.BudgetOnWidgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetOnWidgetDao : BaseLocalDao<BudgetOnWidgetEntity> {

    @Query("DELETE FROM BudgetOnWidget")
    suspend fun deleteAllBudgetsOnWidget()

    @Query("SELECT * FROM BudgetOnWidget")
    override fun getAllEntities(): Flow<List<BudgetOnWidgetEntity>>

}