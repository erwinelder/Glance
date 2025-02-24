package com.ataglance.walletglance.category.domain.usecase

import com.ataglance.walletglance.budget.domain.usecase.DeleteBudgetsByCategoriesUseCase
import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.mapper.toDataModel
import com.ataglance.walletglance.categoryCollection.domain.usecase.DeleteCategoryCollectionsAndAssociationsByCategoriesUseCase
import com.ataglance.walletglance.core.utils.excludeItems

class SaveCategoriesUseCaseImpl(
    private val categoryRepository: CategoryRepository,
    private val deleteCollectionsAndAssociationsByCategoriesUseCase:
    DeleteCategoryCollectionsAndAssociationsByCategoriesUseCase,
    private val deleteBudgetsByCategoriesUseCase: DeleteBudgetsByCategoriesUseCase
) : SaveCategoriesUseCase {

    override suspend fun upsert(categories: List<Category>) {
        categoryRepository.upsertCategories(categories = categories.map(Category::toDataModel))
    }

    override suspend fun save(categories: List<Category>) {
        val entitiesToSave = categories.map(Category::toDataModel)
        val entitiesToDelete = categoryRepository.getAllCategories()
            .excludeItems(entitiesToSave) { it.id }

        if (entitiesToDelete.isNotEmpty()) {
            val idsToDelete = entitiesToDelete.map { it.id }
            deleteCollectionsAndAssociationsByCategoriesUseCase.execute(categoryIds = idsToDelete)
            deleteBudgetsByCategoriesUseCase.delete(categoryIds = idsToDelete)
            categoryRepository.deleteAndUpsertCategories(
                toDelete = entitiesToDelete, toUpsert = entitiesToSave
            )
        } else {
            categoryRepository.upsertCategories(categories = entitiesToSave)
        }
    }

}