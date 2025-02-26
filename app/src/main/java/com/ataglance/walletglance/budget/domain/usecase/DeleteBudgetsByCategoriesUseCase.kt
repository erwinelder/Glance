package com.ataglance.walletglance.budget.domain.usecase

interface DeleteBudgetsByCategoriesUseCase {
    suspend fun delete(categoryIds: List<Int>)
}