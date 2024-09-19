package com.ataglance.walletglance.personalization.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ataglance.walletglance.personalization.data.local.model.BudgetOnWidgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetOnWidgetDao {

    @Upsert
    suspend fun upsertBudgetsOnWidget(budgetOnWidgetList: List<BudgetOnWidgetEntity>)

    @Query("DELETE FROM BudgetOnWidget WHERE budgetId NOT IN (:budgetIds)")
    suspend fun deleteBudgetsThatAreNotInList(budgetIds: List<Int>)

    @Query("SELECT * FROM BudgetOnWidget")
    fun getAllBudgetsOnWidget(): Flow<List<BudgetOnWidgetEntity>>

}