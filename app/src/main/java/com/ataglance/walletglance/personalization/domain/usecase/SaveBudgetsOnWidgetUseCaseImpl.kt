package com.ataglance.walletglance.personalization.domain.usecase

import com.ataglance.walletglance.personalization.data.repository.BudgetOnWidgetRepository
import com.ataglance.walletglance.personalization.mapper.toBudgetOnWidgetEntities
import kotlinx.coroutines.flow.first

class SaveBudgetsOnWidgetUseCaseImpl(
    private val budgetOnWidgetRepository: BudgetOnWidgetRepository
) : SaveBudgetsOnWidgetUseCase {
    override suspend fun execute(budgetsIds: List<Int>) {
        val budgets = budgetsIds.toBudgetOnWidgetEntities()
        val currBudgets = budgetOnWidgetRepository.getAllBudgetsOnWidget().first()

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