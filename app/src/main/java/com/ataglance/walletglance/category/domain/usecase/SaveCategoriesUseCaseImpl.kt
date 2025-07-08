package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.mapper.toDataModel
import com.ataglance.walletglance.core.utils.excludeItems

class SaveCategoriesUseCaseImpl(
    private val categoryRepository: CategoryRepository
) : SaveCategoriesUseCase {

    override suspend fun saveAndDeleteRest(categories: List<Category>) {
        val entitiesToUpsert = categories.map { it.toDataModel() }
        val entitiesToDelete = categoryRepository.getAllCategories()
            .excludeItems(entitiesToUpsert) { it.id }

        if (entitiesToDelete.isEmpty()) {
            categoryRepository.upsertCategories(categories = entitiesToUpsert)
        } else {
            categoryRepository.deleteAndUpsertCategories(
                toDelete = entitiesToDelete, toUpsert = entitiesToUpsert
            )
        }
    }

    override suspend fun save(categories: List<Category>) {
        categoryRepository.upsertCategories(categories = categories.map { it.toDataModel() })
    }

}