package com.ataglance.walletglance.budget.domain.usecase

import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity

interface DeleteBudgetsAndAssociationsUseCase {

    suspend fun delete(budgets: List<BudgetEntity>)

    suspend fun delete(budgets: List<BudgetEntity>, associations: List<BudgetAccountAssociation>)

}