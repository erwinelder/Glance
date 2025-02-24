package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.data.local.model.BudgetOnWidgetEntity
import kotlinx.coroutines.flow.Flow

interface BudgetOnWidgetRepository {

    suspend fun deleteBudgetsOnWidget(budgets: List<BudgetOnWidgetEntity>)

    suspend fun deleteAndUpsertBudgetsOnWidget(
        toDelete: List<BudgetOnWidgetEntity>,
        toUpsert: List<BudgetOnWidgetEntity>
    )

    suspend fun deleteAllBudgetsOnWidgetLocally()

    fun getAllBudgetsOnWidgetFlow(): Flow<List<BudgetOnWidgetEntity>>

    suspend fun getAllBudgetsOnWidget(): List<BudgetOnWidgetEntity>

}