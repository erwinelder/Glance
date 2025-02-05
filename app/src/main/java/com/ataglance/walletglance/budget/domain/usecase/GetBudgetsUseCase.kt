package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.domain.model.BudgetsByType

interface GetBudgetsUseCase {
    suspend fun get(): BudgetsByType
}