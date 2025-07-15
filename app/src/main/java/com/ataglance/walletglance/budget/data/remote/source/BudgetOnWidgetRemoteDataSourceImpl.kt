package com.ataglance.walletglance.budget.data.remote.source

import com.ataglance.walletglance.budget.data.remote.model.BudgetOnWidgetDto

class BudgetOnWidgetRemoteDataSourceImpl() : BudgetOnWidgetRemoteDataSource {

    override suspend fun getUpdateTime(userId: Int): Long? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun synchronizeBudgetsOnWidget(
        budgets: List<BudgetOnWidgetDto>,
        timestamp: Long,
        userId: Int
    ): Boolean {
        // TODO("Not yet implemented")
        return false
    }

    override suspend fun synchronizeBudgetsOnWidgetAndGetAfterTimestamp(
        budgets: List<BudgetOnWidgetDto>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<BudgetOnWidgetDto>? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun getBudgetsOnWidgetAfterTimestamp(
        timestamp: Long,
        userId: Int
    ): List<BudgetOnWidgetDto>? {
        // TODO("Not yet implemented")
        return null
    }

}