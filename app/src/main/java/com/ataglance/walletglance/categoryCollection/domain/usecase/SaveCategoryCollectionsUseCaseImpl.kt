package com.ataglance.walletglance.categoryCollection.domain.usecase

import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepository
import com.ataglance.walletglance.categoryCollection.data.utils.getAssociationsThatAreNotInList
import com.ataglance.walletglance.categoryCollection.data.utils.getEntitiesThatAreNotInList
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.mapper.divideIntoCollectionsAndAssociations

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

        val collectionsToDelete = originalCollections.getEntitiesThatAreNotInList(newCollections)
        val associationsToDelete = originalAssociations.getAssociationsThatAreNotInList(newAssociations)

        categoryCollectionRepository.deleteAndUpsertCollectionsAndAssociations(
            collectionsToDelete = collectionsToDelete,
            collectionsToUpsert = newCollections,
            associationsToDelete = associationsToDelete,
            associationsToUpsert = newAssociations
        )
    }
}