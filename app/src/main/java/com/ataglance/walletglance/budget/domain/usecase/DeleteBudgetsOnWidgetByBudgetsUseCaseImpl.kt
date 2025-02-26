package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.repository.BudgetOnWidgetRepository

class DeleteBudgetsOnWidgetByBudgetsUseCaseImpl(
    private val budgetOnWidgetRepository: BudgetOnWidgetRepository
) : DeleteBudgetsOnWidgetByBudgetsUseCase {
    override suspend fun delete(budgetIds: List<Int>) {
        val budgetsOnWidgetToDelete = budgetOnWidgetRepository.getAllBudgetsOnWidget()
            .filter { it.budgetId in budgetIds }
        budgetOnWidgetRepository.deleteBudgetsOnWidget(budgets = budgetsOnWidgetToDelete)
    }
}