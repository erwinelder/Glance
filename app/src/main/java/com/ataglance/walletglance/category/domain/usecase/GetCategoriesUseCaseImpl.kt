package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.category.domain.mapper.group
import com.ataglance.walletglance.category.domain.mapper.groupByType
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.utils.asChar
import com.ataglance.walletglance.category.mapper.toDomainModels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class GetCategoriesUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : GetCategoriesUseCase {

    override fun getGroupedAsFlow(): Flow<GroupedCategoriesByType> {
        return categoryRepository.getAllCategories().map { categoryEntities ->
            categoryEntities.toDomainModels().groupByType()
        }
    }

    override suspend fun getGrouped(): GroupedCategoriesByType {
        return categoryRepository.getAllCategories().firstOrNull()
            ?.toDomainModels()
            ?.groupByType()
            ?: GroupedCategoriesByType()
    }

    override suspend fun getOfExpenseType(): List<GroupedCategories> {
        return categoryRepository.getCategoriesByType(type = CategoryType.Expense.asChar())
            .toDomainModels()
            .group()
    }

    override suspend fun getAsList(): List<Category> {
        return categoryRepository.getAllCategories().firstOrNull()
            ?.toDomainModels()
            .orEmpty()
    }

}