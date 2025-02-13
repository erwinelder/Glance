package com.ataglance.walletglance.categoryCollection.domain.usecase

import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepository
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories
import com.ataglance.walletglance.categoryCollection.domain.utils.toCollectionsWithIds
import com.ataglance.walletglance.categoryCollection.mapper.divideIntoCollectionsAndAssociations
import com.ataglance.walletglance.core.utils.excludeItems

class SaveCategoryCollectionsUseCaseImpl(
    private val categoryCollectionRepository: CategoryCollectionRepository
) : SaveCategoryCollectionsUseCase {
    override suspend fun execute(collections: List<CategoryCollectionWithCategories>) {
        val (currCollections, currAssociations) = categoryCollectionRepository
            .getAllCollectionsAndAssociations()
        val (newCollections, newAssociations) = collections
            .toCollectionsWithIds()
            .divideIntoCollectionsAndAssociations()

        val collectionsToDelete = currCollections.excludeItems(newCollections) { it.id }
        val associationsToDelete = currAssociations.excludeItems(newAssociations) {
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