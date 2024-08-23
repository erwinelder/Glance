package com.ataglance.walletglance.data.utils

import com.ataglance.walletglance.data.local.entities.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.data.local.entities.CategoryCollectionEntity

fun List<CategoryCollectionEntity>.getIdsThatAreNotInList(
    list: List<CategoryCollectionEntity>
): List<Int> {
    return this
        .filter { collection ->
            list.find { it.id == collection.id } == null
        }
        .map { it.id }
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