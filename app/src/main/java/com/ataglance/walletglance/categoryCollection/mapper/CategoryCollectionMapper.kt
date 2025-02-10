package com.ataglance.walletglance.categoryCollection.mapper

import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.domain.utils.asCategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.utils.asChar


fun Pair<List<CategoryCollectionEntity>, List<CategoryCollectionCategoryAssociation>>.groupByType():
        CategoryCollectionsWithIdsByType
{
    return first
        .map { collection ->
            CategoryCollectionWithIds(
                id = collection.id,
                orderNum = collection.orderNum,
                type = collection.type.asCategoryCollectionType(),
                name = collection.name,
                categoriesIds = second
                    .filter { it.categoryCollectionId == collection.id }
                    .map { it.categoryId }
            )
        }
        .groupBy { it.type }
        .let { CategoryCollectionsWithIdsByType.fromGroupedCollections(map = it) }
}


fun List<CategoryCollectionWithIds>.divideIntoCollectionsAndAssociations():
        Pair<List<CategoryCollectionEntity>, List<CategoryCollectionCategoryAssociation>>
{
    val collectionEntities = this.map { collectionWithIds ->
        CategoryCollectionEntity(
            id = collectionWithIds.id,
            orderNum = collectionWithIds.orderNum,
            type = collectionWithIds.type.asChar(),
            name = collectionWithIds.name
        )
    }
    val associations = this
        .filter { it.categoriesIds != null }
        .flatMap { collectionWithIds ->
            collectionWithIds.categoriesIds!!.map { categoryId ->
                CategoryCollectionCategoryAssociation(
                    categoryCollectionId = collectionWithIds.id,
                    categoryId = categoryId
                )
            }
        }

    return collectionEntities to associations
}
