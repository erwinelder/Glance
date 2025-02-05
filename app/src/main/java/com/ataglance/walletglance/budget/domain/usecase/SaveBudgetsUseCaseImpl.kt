package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.data.utils.getAssociationsThatAreNotInList
import com.ataglance.walletglance.budget.data.utils.getBudgetsThatAreNotInList
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.mapper.divideIntoBudgetsAndAssociations

class SaveBudgetsUseCaseImpl(
    private val budgetRepository: BudgetRepository
) : SaveBudgetsUseCase {
    override suspend fun execute(budgetsToSave: List<Budget>, currentBudgets: List<Budget>) {
        val (newBudgets, newAssociations) = budgetsToSave.divideIntoBudgetsAndAssociations()
        val (originalBudgets, originalAssociations) = currentBudgets.divideIntoBudgetsAndAssociations()

        val budgetsToDelete = originalBudgets.getBudgetsThatAreNotInList(newBudgets)
        val associationsToDelete = originalAssociations.getAssociationsThatAreNotInList(newAssociations)

        budgetRepository.deleteAndUpsertBudgetsAndAssociations(
            budgetsToDelete = budgetsToDelete,
            budgetsToUpsert = newBudgets,
            associationsToDelete = associationsToDelete,
            associationsToUpsert = newAssociations
        )
    }
}