package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.repository.BudgetRepository
import com.ataglance.walletglance.core.utils.excludeItems

class DeleteBudgetAccountAssociationsByAccountsUseCaseImpl(
    private val budgetRepository: BudgetRepository,
    private val deleteBudgetsAndAssociationsUseCase: DeleteBudgetsAndAssociationsUseCase
) : DeleteBudgetAccountAssociationsByAccountsUseCase {
    override suspend fun delete(accountIds: List<Int>) {
        val (budgets, associations) = budgetRepository.getAllBudgetsAndAssociations()

        val accountsAssociations = associations.filter { it.accountId in accountIds }
        val leftAssociationsBudgetIds = associations
            .excludeItems(accountsAssociations) { it.budgetId to it.accountId }
            .map { it.budgetId }
        val budgetsToDelete = budgets.filter { it.id !in leftAssociationsBudgetIds }

        deleteBudgetsAndAssociationsUseCase.delete(
            budgets = budgetsToDelete, associations = accountsAssociations
        )
    }
}