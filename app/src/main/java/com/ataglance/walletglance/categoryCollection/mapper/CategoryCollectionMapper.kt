package com.ataglance.walletglance.categoryCollection.mapper

import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionEntity
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.domain.utils.asChar



fun Map<String, Any?>.toCategoryCollectionEntity(): CategoryCollectionEntity {
    return CategoryCollectionEntity(
        id = this["id"] as Int,
        orderNum = this["orderNum"] as Int,
        type = this["type"] as Char,
        name = this["name"] as String
    )
}

fun CategoryCollectionEntity.toMap(timestamp: Long): HashMap<String, Any> {
    return hashMapOf(
        "LMT" to timestamp,
        "id" to id,
        "orderNum" to orderNum,
        "type" to type,
        "name" to name
    )
}



fun Map<String, Any?>.toCategoryCollectionCategoryAssociation():
        CategoryCollectionCategoryAssociation
{
    return CategoryCollectionCategoryAssociation(
        categoryCollectionId = this["categoryCollectionId"] as Int,
        categoryId = this["categoryId"] as Int
    )
}

fun CategoryCollectionCategoryAssociation.toMap(timestamp: Long): HashMap<String, Any> {
    return hashMapOf(
        "LMT" to timestamp,
        "categoryCollectionId" to categoryCollectionId,
        "categoryId" to categoryId
    )
}



fun transformCategCollectionsAndCollectionCategAssociationsToCollectionsWithIds(
    collectionList: List<CategoryCollectionEntity>,
    collectionCategoryAssociationList: List<CategoryCollectionCategoryAssociation>
): CategoryCollectionsWithIdsByType {
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
                    CategoryCollectionsWithIdsByType(
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
