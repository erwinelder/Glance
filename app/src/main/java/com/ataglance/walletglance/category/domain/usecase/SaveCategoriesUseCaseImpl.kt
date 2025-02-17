package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.mapper.toDataModel
import com.ataglance.walletglance.core.utils.excludeItems

class SaveCategoriesUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : SaveCategoriesUseCase {

    override suspend fun upsert(categories: List<Category>) {
        categoryRepository.upsertCategories(categories = categories.map(Category::toDataModel))
    }

    override suspend fun save(categories: List<Category>) {
        val entitiesToSave = categories.map(Category::toDataModel)
        val entitiesToDelete = categoryRepository.getAllCategories()
            .excludeItems(entitiesToSave) { it.id }

        if (entitiesToDelete.isNotEmpty()) {
            categoryRepository.deleteAndUpsertCategories(
                toDelete = entitiesToDelete, toUpsert = entitiesToSave
            )
        } else {
            categoryRepository.upsertCategories(categories = entitiesToSave)
        }
    }

}