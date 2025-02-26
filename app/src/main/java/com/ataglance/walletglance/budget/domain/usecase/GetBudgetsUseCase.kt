package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import kotlinx.coroutines.flow.Flow

interface GetBudgetsUseCase {

    fun getGroupedByTypeFlow(): Flow<BudgetsByType>

    suspend fun getGroupedByType(): BudgetsByType

    suspend fun get(id: Int): Budget?

    suspend fun get(id: Int, accounts: List<Account>): Budget?

}