package com.ataglance.walletglance.budget.data.remote.source

import com.ataglance.walletglance.budget.data.remote.model.BudgetDtoWithAssociations

interface BudgetRemoteDataSource {

    suspend fun getUpdateTime(userId: Int): Long?

    suspend fun synchronizeBudgetsWithAssociations(
        budgets: List<BudgetDtoWithAssociations>,
        timestamp: Long,
        userId: Int
    ): Boolean

    suspend fun synchronizeBudgetsWithAssociationsAndGetAfterTimestamp(
        budgets: List<BudgetDtoWithAssociations>,
        timestamp: Long,
        userId: Int,
        localTimestamp: Long
    ): List<BudgetDtoWithAssociations>?

    suspend fun getBudgetsWithAssociationsAfterTimestamp(
        timestamp: Long,
        userId: Int
    ): List<BudgetDtoWithAssociations>?

}