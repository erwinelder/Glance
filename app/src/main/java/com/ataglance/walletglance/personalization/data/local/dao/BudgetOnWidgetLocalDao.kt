package com.ataglance.walletglance.personalization.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.core.data.local.dao.BaseLocalDao
import com.ataglance.walletglance.personalization.data.local.model.BudgetOnWidgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetOnWidgetLocalDao : BaseLocalDao<BudgetOnWidgetEntity> {

    @Query("DELETE FROM BudgetOnWidget")
    suspend fun deleteAllBudgetsOnWidget()

    @Query("SELECT * FROM BudgetOnWidget")
    fun getAllBudgetsOnWidget(): Flow<List<BudgetOnWidgetEntity>>

}