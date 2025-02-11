package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.mapper.toDataModel
import com.ataglance.walletglance.core.utils.excludeItems

class SaveCategoriesUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : SaveCategoriesUseCase {

    override suspend fun execute(categories: List<Category>) {
        categoryRepository.upsertCategories(categories = categories.map(Category::toDataModel))
    }

    override suspend fun execute(
        categoriesToSave: List<Category>,
        currentCategories: List<Category>
    ) {
        val entitiesToSave = categoriesToSave.map(Category::toDataModel)
        val entitiesToDelete = currentCategories
            .map(Category::toDataModel)
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