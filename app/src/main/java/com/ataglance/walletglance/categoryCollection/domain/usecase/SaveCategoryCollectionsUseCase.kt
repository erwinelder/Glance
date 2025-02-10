package com.ataglance.walletglance.categoryCollection.domain.usecase

import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds

interface SaveCategoryCollectionsUseCase {
    suspend fun execute(
        collectionsToSave: List<CategoryCollectionWithIds>,
        currentCollections: List<CategoryCollectionWithIds>
    )
}