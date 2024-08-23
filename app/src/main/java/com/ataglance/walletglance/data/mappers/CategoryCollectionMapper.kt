package com.ataglance.walletglance.data.mappers

import com.ataglance.walletglance.domain.categoryCollections.CategoryCollectionType
import com.ataglance.walletglance.domain.categoryCollections.CategoryCollectionWithIds
import com.ataglance.walletglance.domain.categoryCollections.CategoryCollectionsWithIds
import com.ataglance.walletglance.data.local.entities.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.data.local.entities.CategoryCollectionEntity
import com.ataglance.walletglance.domain.utils.asChar



fun transformCategCollectionsAndCollectionCategAssociationsToCollectionsWithIds(
    collectionList: List<CategoryCollectionEntity>,
    collectionCategoryAssociationList: List<CategoryCollectionCategoryAssociation>
): CategoryCollectionsWithIds {
    return collectionList
        .map { collection ->
            CategoryCollectionWithIds(
                id = collection.id,
                orderNum = collection.orderNum,
                type = collection.getCategoryType(),
                name = collection.name,
                categoriesIds = collectionCategoryAssociationList
                    .filter { it.categoryCollectionId == collection.id }
                    .map { it.categoryId }
            )
        }
        .partition { it.type == CategoryCollectionType.Expense }
        .let { expenseAndOtherCollections ->
            expenseAndOtherCollections.second
                .partition { it.type == CategoryCollectionType.Income }
                .let { incomeAndMixedCollections ->
                    CategoryCollectionsWithIds(
                        expense = expenseAndOtherCollections.first,
                        income = incomeAndMixedCollections.first,
                        mixed = incomeAndMixedCollections.second
                    )
                }
        }
}



fun List<CategoryCollectionWithIds>.divideIntoCollectionsAndAssociations():
        Pair<List<CategoryCollectionEntity>, List<CategoryCollectionCategoryAssociation>>
{
    val categoryCollectionList = this.map { collectionWithIds ->
        CategoryCollectionEntity(
            id = collectionWithIds.id,
            orderNum = collectionWithIds.orderNum,
            type = collectionWithIds.type.asChar(),
            name = collectionWithIds.name
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