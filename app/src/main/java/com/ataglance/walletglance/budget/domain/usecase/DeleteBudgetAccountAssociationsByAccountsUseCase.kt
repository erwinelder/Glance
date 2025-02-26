package com.ataglance.walletglance.budget.domain.usecase

interface DeleteBudgetAccountAssociationsByAccountsUseCase {
    suspend fun delete(accountIds: List<Int>)
}