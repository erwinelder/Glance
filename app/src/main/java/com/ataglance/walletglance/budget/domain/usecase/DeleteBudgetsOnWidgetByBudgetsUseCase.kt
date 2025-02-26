package com.ataglance.walletglance.budget.domain.usecase

interface DeleteBudgetsOnWidgetByBudgetsUseCase {
    suspend fun delete(budgetIds: List<Int>)
}