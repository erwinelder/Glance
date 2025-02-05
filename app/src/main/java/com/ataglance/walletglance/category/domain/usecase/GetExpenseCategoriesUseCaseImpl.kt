package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.category.domain.mapper.groupCategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategories
import com.ataglance.walletglance.category.domain.utils.asChar
import com.ataglance.walletglance.category.mapper.toDomainModels

class GetExpenseCategoriesUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : GetExpenseCategoriesUseCase {
    override suspend fun execute(): List<CategoryWithSubcategories> {
        return categoryRepository.getCategoriesByType(type = CategoryType.Expense.asChar())
            .toDomainModels()
            .groupCategoriesWithSubcategories()
    }
}