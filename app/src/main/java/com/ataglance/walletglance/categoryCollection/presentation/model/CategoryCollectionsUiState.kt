package com.ataglance.walletglance.categoryCollection.presentation.model

import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.domain.utils.toggle

data class CategoryCollectionsUiState(
    val collections: List<CategoryCollectionWithIds> = emptyList(),
    val activeCollection: CategoryCollectionWithIds = CategoryCollectionWithIds(),
    val activeType: CategoryCollectionType = CategoryCollectionType.Mixed
) {

    fun setCollections(
        collections: CategoryCollectionsWithIdsByType,
        defaultCollectionName: String
    ): CategoryCollectionsUiState {
        return copy(
            collections = collections.getByType(activeType),
            activeCollection = CategoryCollectionWithIds(
                type = activeType, name = defaultCollectionName
            )
        )
    }

    fun toggleCollectionType(
        collectionsByType: CategoryCollectionsWithIdsByType,
        defaultCollectionName: String
    ): CategoryCollectionsUiState {
        val type = activeType.toggle()
        return copy(
            collections = collectionsByType.getByType(type),
            activeCollection = CategoryCollectionWithIds(type = type, name = defaultCollectionName),
            activeType = type
        )
    }

    fun selectCollection(collectionId: Int): CategoryCollectionsUiState {
        return copy(
            activeCollection = collections.find { it.id == collectionId } ?: activeCollection
        )
    }

}
