package com.ataglance.walletglance.categoryCollection.domain.usecase

import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepository
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories
import com.ataglance.walletglance.categoryCollection.mapper.toDataModelWithAssociations
import com.ataglance.walletglance.core.utils.excludeItems

class SaveCategoryCollectionsUseCaseImpl(
    private val categoryCollectionRepository: CategoryCollectionRepository
) : SaveCategoryCollectionsUseCase {

    override suspend fun saveAndDeleteRest(collections: List<CategoryCollectionWithCategories>) {
        val currentCollections = categoryCollectionRepository.getAllCollections()

        val collectionsToUpsert = collections.mapNotNull { it.toDataModelWithAssociations() }
        val collectionsToDelete = currentCollections.excludeItems(
            items = collectionsToUpsert,
            keySelector1 = { it.id },
            keySelector2 = { it.collectionId }
        )

        categoryCollectionRepository.deleteAndUpsertCollectionsWithAssociations(
            toDelete = collectionsToDelete,
            toUpsert = collectionsToUpsert
        )
    }

}