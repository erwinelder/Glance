package com.ataglance.walletglance.budget.data.local.source

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.budget.data.local.model.BudgetOnWidgetEntity
import kotlinx.coroutines.flow.Flow

interface BudgetOnWidgetLocalDataSource {

    suspend fun getUpdateTime(): Long?

    suspend fun saveUpdateTime(timestamp: Long)

    suspend fun upsertBudgetsOnWidget(budgets: List<BudgetOnWidgetEntity>, timestamp: Long)

    suspend fun deleteBudgetsOnWidget(budgets: List<BudgetOnWidgetEntity>, timestamp: Long)

    suspend fun synchroniseBudgetsOnWidget(
        budgetsToSync: EntitiesToSync<BudgetOnWidgetEntity>,
        timestamp: Long
    )

    suspend fun deleteAllBudgetsOnWidget(timestamp: Long)

    fun getAllBudgetsOnWidget(): Flow<List<BudgetOnWidgetEntity>>

}