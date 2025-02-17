package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.repository.BudgetOnWidgetRepository
import com.ataglance.walletglance.budget.mapper.budgetOnWidget.toBudgetOnWidgetEntities

class SaveBudgetsOnWidgetUseCaseImpl(
    private val budgetOnWidgetRepository: BudgetOnWidgetRepository
) : SaveBudgetsOnWidgetUseCase {
    override suspend fun execute(budgetsIds: List<Int>) {
        val budgets = budgetsIds.toBudgetOnWidgetEntities()
        val currBudgets = budgetOnWidgetRepository.getAllBudgetsOnWidget()

        val budgetsToDelete = currBudgets.filter { budget ->
            budgets.none { it.budgetId == budget.budgetId }
        }
        val budgetsToUpsert = budgets.filter { budget ->
            currBudgets.none { it.budgetId == budget.budgetId }
        }

        budgetOnWidgetRepository.deleteAndUpsertBudgetsOnWidget(
            toDelete = budgetsToDelete, toUpsert = budgetsToUpsert
        )
    }
}