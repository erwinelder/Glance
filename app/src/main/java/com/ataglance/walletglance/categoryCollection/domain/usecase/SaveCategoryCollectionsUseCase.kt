package com.ataglance.walletglance.categoryCollection.domain.usecase

import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories

interface SaveCategoryCollectionsUseCase {
    suspend fun saveAndDeleteRest(collections: List<CategoryCollectionWithCategories>)
}