package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.mapper.divideIntoBudgetsAndAssociations
import com.ataglance.walletglance.core.utils.excludeItems

class SaveBudgetsUseCaseImpl(
    private val budgetRepository: BudgetRepository
) : SaveBudgetsUseCase {
    override suspend fun execute(budgetsToSave: List<Budget>, currentBudgets: List<Budget>) {
        val (newBudgets, newAssociations) = budgetsToSave.divideIntoBudgetsAndAssociations()
        val (originalBudgets, originalAssociations) = currentBudgets.divideIntoBudgetsAndAssociations()

        val budgetsToDelete = originalBudgets.excludeItems(newBudgets) { it.id }
        val associationsToDelete = originalAssociations.excludeItems(newAssociations) {
            it.budgetId to it.accountId
        }

        budgetRepository.deleteAndUpsertBudgetsAndAssociations(
            budgetsToDelete = budgetsToDelete,
            budgetsToUpsert = newBudgets,
            associationsToDelete = associationsToDelete,
            associationsToUpsert = newAssociations
        )
    }
}