package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import kotlinx.coroutines.flow.Flow

interface GetFilledBudgetsByTypeUseCase {

    fun getAsFlow(): Flow<BudgetsByType>

    suspend fun get(): BudgetsByType

}