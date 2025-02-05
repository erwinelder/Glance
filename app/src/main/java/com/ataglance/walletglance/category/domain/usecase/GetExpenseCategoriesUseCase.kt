package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategories

interface GetExpenseCategoriesUseCase {
    suspend fun execute(): List<CategoryWithSubcategories>
}