package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.category.domain.mapper.toCategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.mapper.toDomainModels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class GetAllCategoriesUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : GetAllCategoriesUseCase {

    override fun getAsFlow(): Flow<CategoriesWithSubcategories> {
        return categoryRepository.getAllCategories().map { categoryEntities ->
            categoryEntities.toDomainModels().toCategoriesWithSubcategories()
        }
    }

    override suspend fun get(): CategoriesWithSubcategories {
        return categoryRepository.getAllCategories().firstOrNull()
            ?.toDomainModels()
            ?.toCategoriesWithSubcategories()
            ?: CategoriesWithSubcategories(emptyList())
    }

}