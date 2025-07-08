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
    suspend fun upsertBudgetsOnWidget(budgets: List<BudgetOnWidgetEntity>)

    @Delete
    suspend fun deleteBudgetsOnWidget(budgets: List<BudgetOnWidgetEntity>)

    @Transaction
    suspend fun deleteAndUpsertBudgetsOnWidget(
        toDelete: List<BudgetOnWidgetEntity>,
        toUpsert: List<BudgetOnWidgetEntity>
    ) {
        deleteBudgetsOnWidget(budgets = toDelete)
        upsertBudgetsOnWidget(budgets = toUpsert)
    }

    @Query("SELECT * FROM budget_on_widget WHERE timestamp > :timestamp")
    suspend fun getBudgetsOnWidgetAfterTimestamp(timestamp: Long): List<BudgetOnWidgetEntity>

    @Query("SELECT * FROM budget_on_widget WHERE deleted = 0")
    fun getAllBudgetsOnWidgetAsFlow(): Flow<List<BudgetOnWidgetEntity>>

}