package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.mapper.budget.toDataModelWithAssociations
import com.ataglance.walletglance.core.utils.excludeItems

class SaveBudgetsUseCaseImpl(
    private val budgetRepository: BudgetRepository
) : SaveBudgetsUseCase {
    override suspend fun saveAndDeleteRest(budgets: List<Budget>) {
        val currentBudgets = budgetRepository.getAllBudgets()

        val budgetsToUpsert = budgets.map { it.toDataModelWithAssociations() }
        val budgetsToDelete = currentBudgets.excludeItems(
            items = budgetsToUpsert,
            keySelector1 = { it.id },
            keySelector2 = { it.budgetId }
        )

        budgetRepository.deleteAndUpsertBudgetsWithAssociations(
            toDelete = budgetsToDelete,
            toUpsert = budgetsToUpsert
        )
    }
}