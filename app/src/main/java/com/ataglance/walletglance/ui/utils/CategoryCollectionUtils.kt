package com.ataglance.walletglance.ui.utils

import com.ataglance.walletglance.domain.entities.CategoryCollection
import com.ataglance.walletglance.domain.entities.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.ui.viewmodels.CategoryCollectionUiState

fun transformCategoryCollectionsAndCollectionCategoryAssociationsToOneList(
    collectionList: List<CategoryCollection>,
    collectionCategoryAssociationList: List<CategoryCollectionCategoryAssociation>
): List<CategoryCollectionUiState> {
    return collectionList
        .map { collection ->
            CategoryCollectionUiState(
                id = collection.id,
                orderNum = collection.orderNum,
                name = collection.name,
                categoriesIds = collectionCategoryAssociationList
                    .filter { it.categoryCollectionId == collection.id }
                    .map { it.categoryCollectionId }
            )
        }
        .sortedBy { it.orderNum }
}

fun List<CategoryCollectionUiState>.breakOnCollectionsAndAssociations():
        Pair<List<CategoryCollection>, List<CategoryCollectionCategoryAssociation>>
{
    val categoryCollectionList = this.map { collectionUiState ->
        CategoryCollection(
            id = collectionUiState.id,
            orderNum = collectionUiState.orderNum,
            name = collectionUiState.name
        )
    }
    val listOfCollectionCategoryAssociationList = this
        .filter { it.categoriesIds != null }
        .flatMap { collectionUiState ->
            collectionUiState.categoriesIds!!.map { categoryId ->
                CategoryCollectionCategoryAssociation(
                    categoryCollectionId = collectionUiState.id,
                    categoryId = categoryId
                )
            }
        }
    return categoryCollectionList to listOfCollectionCategoryAssociationList
}

fun List<CategoryCollection>.getIdsThatAreNotInList(
    list: List<CategoryCollection>
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