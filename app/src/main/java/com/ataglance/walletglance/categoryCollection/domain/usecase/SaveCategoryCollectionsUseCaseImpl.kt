package com.ataglance.walletglance.categoryCollection.domain.usecase

import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepository
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.mapper.divideIntoCollectionsAndAssociations
import com.ataglance.walletglance.core.utils.excludeItems

class SaveCategoryCollectionsUseCaseImpl(
    private val categoryCollectionRepository: CategoryCollectionRepository
) : SaveCategoryCollectionsUseCase {
    override suspend fun execute(
        collectionsToSave: List<CategoryCollectionWithIds>,
        currentCollections: List<CategoryCollectionWithIds>
    ) {
        val (newCollections, newAssociations) = collectionsToSave
            .divideIntoCollectionsAndAssociations()
        val (originalCollections, originalAssociations) = currentCollections
            .divideIntoCollectionsAndAssociations()

        val collectionsToDelete = originalCollections.excludeItems(newCollections) { it.id }
        val associationsToDelete = originalAssociations.excludeItems(newAssociations) {
            it.categoryCollectionId to it.categoryId
        }

        categoryCollectionRepository.deleteAndUpsertCollectionsAndAssociations(
            collectionsToDelete = collectionsToDelete,
            collectionsToUpsert = newCollections,
            associationsToDelete = associationsToDelete,
            associationsToUpsert = newAssociations
        )
    }
}