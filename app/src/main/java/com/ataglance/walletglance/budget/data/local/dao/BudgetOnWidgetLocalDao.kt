package com.ataglance.walletglance.budget.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.budget.data.local.model.BudgetOnWidgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetOnWidgetLocalDao {

    @Upsert
    suspend fun upsertBudgets(budgets: List<BudgetOnWidgetEntity>)

    @Delete
    suspend fun deleteBudgets(budgets: List<BudgetOnWidgetEntity>)

    @Transaction
    suspend fun deleteAndUpsertBudgets(
        toDelete: List<BudgetOnWidgetEntity>,
        toUpsert: List<BudgetOnWidgetEntity>
    ) {
        deleteBudgets(toDelete)
        upsertBudgets(toUpsert)
    }

    @Query("DELETE FROM BudgetOnWidget")
    suspend fun deleteAllBudgetsOnWidget()

    @Query("SELECT * FROM BudgetOnWidget")
    fun getAllBudgetsOnWidget(): Flow<List<BudgetOnWidgetEntity>>

}