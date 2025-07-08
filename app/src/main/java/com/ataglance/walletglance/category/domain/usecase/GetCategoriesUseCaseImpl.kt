package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.category.domain.mapper.group
import com.ataglance.walletglance.category.domain.mapper.groupByType
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.mapper.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCategoriesUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : GetCategoriesUseCase {

    override fun getGroupedAsFlow(): Flow<GroupedCategoriesByType> {
        return categoryRepository.getAllCategoriesAsFlow().map { categories ->
            categories.mapNotNull { it.toDomainModel() }.groupByType()
        }
    }

    override suspend fun getGrouped(): GroupedCategoriesByType {
        return categoryRepository.getAllCategories().mapNotNull { it.toDomainModel() }.groupByType()
    }

    override suspend fun getOfExpenseType(): List<GroupedCategories> {
        return categoryRepository.getCategoriesByType(type = CategoryType.Expense.asChar())
            .mapNotNull { it.toDomainModel() }
            .group()
    }

    override suspend fun getAsList(): List<Category> {
        return categoryRepository.getAllCategories().mapNotNull { it.toDomainModel() }
    }

}