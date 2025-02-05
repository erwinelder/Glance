package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.domain.model.Budget

interface SaveBudgetsUseCase {
    suspend fun execute(budgetsToSave: List<Budget>, currentBudgets: List<Budget>)
}