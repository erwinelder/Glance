package com.ataglance.walletglance.categoryCollection.data.utils

import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionEntity

fun List<CategoryCollectionEntity>.getEntitiesThatAreNotInList(
    list: List<CategoryCollectionEntity>
): List<CategoryCollectionEntity> {
    return this.filter { collection ->
        list.find { it.id == collection.id } == null
    }
}

fun List<CategoryCollectionCategoryAssociation>.getAssociationsThatAreNotInList(
    list: List<CategoryCollectionCategoryAssociation>
): List<CategoryCollectionCategoryAssociation> {
    return this
        .filter { association ->
            list.find {
                it.categoryCollectionId == association.categoryCollectionId &&
                        it.categoryId == association.categoryId
            } == null
        }
}