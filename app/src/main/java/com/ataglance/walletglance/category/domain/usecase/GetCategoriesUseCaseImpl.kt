package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.category.domain.mapper.groupCategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.mapper.toCategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategories
import com.ataglance.walletglance.category.domain.utils.asChar
import com.ataglance.walletglance.category.mapper.toDomainModels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class GetCategoriesUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : GetCategoriesUseCase {

    override fun getGroupedAsFlow(): Flow<CategoriesWithSubcategories> {
        return categoryRepository.getAllCategories().map { categoryEntities ->
            categoryEntities.toDomainModels().toCategoriesWithSubcategories()
        }
    }

    override suspend fun getGrouped(): CategoriesWithSubcategories {
        return categoryRepository.getAllCategories().firstOrNull()
            ?.toDomainModels()
            ?.toCategoriesWithSubcategories()
            ?: CategoriesWithSubcategories()
    }

    override suspend fun getOfExpenseType(): List<CategoryWithSubcategories> {
        return categoryRepository.getCategoriesByType(type = CategoryType.Expense.asChar())
            .toDomainModels()
            .groupCategoriesWithSubcategories()
    }

    override suspend fun getSimple(): List<Category> {
        return categoryRepository.getAllCategories().firstOrNull()
            ?.toDomainModels()
            .orEmpty()
    }

}