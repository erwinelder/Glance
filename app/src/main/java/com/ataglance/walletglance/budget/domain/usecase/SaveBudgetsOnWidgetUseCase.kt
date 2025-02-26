package com.ataglance.walletglance.budget.domain.usecase

interface SaveBudgetsOnWidgetUseCase {
    suspend fun execute(budgetsIds: List<Int>)
}