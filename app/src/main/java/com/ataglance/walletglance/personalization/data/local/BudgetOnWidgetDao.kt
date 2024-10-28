package com.ataglance.walletglance.personalization.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.core.data.local.BaseDao
import com.ataglance.walletglance.personalization.data.model.BudgetOnWidgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetOnWidgetDao : BaseDao<BudgetOnWidgetEntity> {

    @Query("DELETE FROM BudgetOnWidget WHERE budgetId NOT IN (:budgetIds)")
    suspend fun deleteBudgetsThatAreNotInList(budgetIds: List<Int>)

    @Query("SELECT * FROM BudgetOnWidget")
    override fun getAllEntities(): Flow<List<BudgetOnWidgetEntity>>

}