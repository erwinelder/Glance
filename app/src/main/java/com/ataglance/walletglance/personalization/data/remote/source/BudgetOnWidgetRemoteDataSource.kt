package com.ataglance.walletglance.personalization.data.remote.source

import com.ataglance.walletglance.core.data.model.EntitiesToSync
import com.ataglance.walletglance.personalization.data.remote.model.BudgetOnWidgetRemoteEntity

interface BudgetOnWidgetRemoteDataSource {

    suspend fun getUpdateTime(userId: String): Long?

    suspend fun saveUpdateTime(timestamp: Long, userId: String)

    suspend fun upsertBudgetsOnWidget(
        budgets: List<BudgetOnWidgetRemoteEntity>,
        timestamp: Long,
        userId: String
    )

    suspend fun synchroniseBudgetsOnWidget(
        budgetsToSync: EntitiesToSync<BudgetOnWidgetRemoteEntity>,
        timestamp: Long,
        userId: String
    )

    suspend fun getBudgetsOnWidgetAfterTimestamp(
        timestamp: Long,
        userId: String
    ): EntitiesToSync<BudgetOnWidgetRemoteEntity>

}