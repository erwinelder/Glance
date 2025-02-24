package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.repository.BudgetRepository

class DeleteBudgetsByCategoriesUseCaseImpl(
    private val budgetRepository: BudgetRepository,
    private val deleteBudgetsAndAssociationsUseCase: DeleteBudgetsAndAssociationsUseCase
) : DeleteBudgetsByCategoriesUseCase {
    override suspend fun delete(categoryIds: List<Int>) {
        val (budgets, associations) = budgetRepository.getAllBudgetsAndAssociations()

        val budgetsToDelete = budgets.filter { it.categoryId in categoryIds }
        val budgetIdsToDelete = budgetsToDelete.map { it.id }
        val associationsToDelete = associations.filter { it.budgetId in budgetIdsToDelete }

        deleteBudgetsAndAssociationsUseCase.delete(
            budgets = budgetsToDelete, associations = associationsToDelete
        )
    }
}