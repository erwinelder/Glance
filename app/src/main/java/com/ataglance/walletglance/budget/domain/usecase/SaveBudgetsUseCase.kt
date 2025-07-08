package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.domain.model.Budget

interface SaveBudgetsUseCase {
    suspend fun saveAndDeleteRest(budgets: List<Budget>)
}