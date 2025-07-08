package com.ataglance.walletglance.categoryCollection.data.utils

import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociationEntity
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntityWithAssociations


fun List<CategoryCollectionEntityWithAssociations>.divide(
): Pair<List<CategoryCollectionEntity>, List<CategoryCollectionCategoryAssociationEntity>> {
    return map { it.asPair() }
        .unzip()
        .let { it.first to it.second.flatten() }
}

fun List<CategoryCollectionEntity>.zipWithAssociations(
    associations: List<CategoryCollectionCategoryAssociationEntity>
): List<CategoryCollectionEntityWithAssociations> {
    return associateWith { budget ->
            if (!budget.deleted) associations.filter { it.collectionId == budget.id }
            else emptyList()
        }
        .map { (budget, associations) ->
            CategoryCollectionEntityWithAssociations(
                collection = budget, associations = associations
            )
        }
}
