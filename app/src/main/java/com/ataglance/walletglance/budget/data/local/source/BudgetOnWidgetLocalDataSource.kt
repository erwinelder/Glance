package com.ataglance.walletglance.budget.data.local.source

import com.ataglance.walletglance.budget.data.local.model.BudgetOnWidgetEntity
import kotlinx.coroutines.flow.Flow

interface BudgetOnWidgetLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun upsertBudgetsOnWidget(budgets: List<BudgetOnWidgetEntity>, timestamp: Long)

    suspend fun deleteBudgetsOnWidget(budgets: List<BudgetOnWidgetEntity>)

    suspend fun deleteAndUpsertBudgetsOnWidget(
        toDelete: List<BudgetOnWidgetEntity>,
        toUpsert: List<BudgetOnWidgetEntity>,
        timestamp: Long
    )

    suspend fun getBudgetsOnWidgetAfterTimestamp(timestamp: Long): List<BudgetOnWidgetEntity>

    fun getAllBudgetsOnWidgetAsFlow(): Flow<List<BudgetOnWidgetEntity>>

    suspend fun getAllBudgetsOnWidget(): List<BudgetOnWidgetEntity>

}