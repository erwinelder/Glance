package com.ataglance.walletglance.budget.data.remote.source

import com.ataglance.walletglance.budget.data.remote.model.BudgetDtoWithAssociations

class BudgetRemoteDataSourceImpl() : BudgetRemoteDataSource {

    override suspend fun getUpdateTime(userId: Int): Long? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun synchronizeBudgetsWithAssociations(
        budgets: List<BudgetDtoWithAssociations>,
        timestamp: Long,
        userId: Int
    ): List<BudgetDtoWithAssociations>? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun synchronizeBudgetsWithAssociationsAndGetAfterTimestamp(
        budgets: List<BudgetDtoWithAssociations>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<BudgetDtoWithAssociations>? {
        // TODO("Not yet implemented")
        return null
    }

    override suspend fun getBudgetsWithAssociationsAfterTimestamp(
        timestamp: Long,
        userId: Int
    ): List<BudgetDtoWithAssociations>? {
        // TODO("Not yet implemented")
        return null
    }

}