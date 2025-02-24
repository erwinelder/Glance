package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.data.repository.BudgetRepository

class DeleteBudgetsAndAssociationsUseCaseImpl(
    private val budgetRepository: BudgetRepository,
    private val deleteBudgetsOnWidgetByBudgetsUseCase: DeleteBudgetsOnWidgetByBudgetsUseCase
) : DeleteBudgetsAndAssociationsUseCase {

    override suspend fun delete(budgets: List<BudgetEntity>) {
        val budgetIds = budgets.map { it.id }
        val associations = budgetRepository.getAllBudgetsAndAssociations().second
            .filter { it.budgetId in budgetIds }

        deleteBudgetsOnWidgetByBudgetsUseCase.delete(budgetIds = budgetIds)
        budgetRepository.deleteBudgetsAndAssociations(
            budgets = budgets, associations = associations
        )
    }

    override suspend fun delete(
        budgets: List<BudgetEntity>,
        associations: List<BudgetAccountAssociation>
    ) {
        val budgetIds = budgets.map { it.id }

        deleteBudgetsOnWidgetByBudgetsUseCase.delete(budgetIds = budgetIds)
        budgetRepository.deleteBudgetsAndAssociations(
            budgets = budgets, associations = associations
        )
    }

}