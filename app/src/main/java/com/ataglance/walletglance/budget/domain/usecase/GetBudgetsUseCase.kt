package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import kotlinx.coroutines.flow.Flow

interface GetBudgetsUseCase {
    fun getAsFlow(): Flow<BudgetsByType>
}