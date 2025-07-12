package com.ataglance.walletglance.budget.data.remote.source

import com.ataglance.walletglance.budget.data.remote.model.BudgetOnWidgetDto

interface BudgetOnWidgetRemoteDataSource {

    suspend fun getUpdateTime(userId: Int): Long?

    suspend fun synchronizeBudgetsOnWidget(
        budgets: List<BudgetOnWidgetDto>,
        timestamp: Long,
        userId: Int
    ): Boolean

    suspend fun synchronizeBudgetsOnWidgetAndGetAfterTimestamp(
        budgets: List<BudgetOnWidgetDto>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<BudgetOnWidgetDto>?

    suspend fun getBudgetsOnWidgetAfterTimestamp(
        timestamp: Long,
        userId: Int
    ): List<BudgetOnWidgetDto>?

}