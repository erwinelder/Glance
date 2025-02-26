package com.ataglance.walletglance.categoryCollection.domain.usecase

import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepository

class DeleteCategoryCollectionsAndAssociationsByCategoriesUseCaseImpl(
    private val categoryCollectionRepository: CategoryCollectionRepository
) :
    DeleteCategoryCollectionsAndAssociationsByCategoriesUseCase {
    override suspend fun execute(categoryIds: List<Int>) {
        val (collections, associations) = categoryCollectionRepository.getAllCollectionsAndAssociations()

        val (associationsToDelete, leftAssociations) = associations
            .partition { it.categoryId in categoryIds }
        val leftCollectionIds = leftAssociations
            .distinctBy { it.categoryCollectionId }
            .map { it.categoryCollectionId }
        val collectionsToDelete = collections.filterNot { it.id in leftCollectionIds }

        categoryCollectionRepository.deleteCollectionsAndAssociations(
            collections = collectionsToDelete, associations = associationsToDelete
        )
    }
}