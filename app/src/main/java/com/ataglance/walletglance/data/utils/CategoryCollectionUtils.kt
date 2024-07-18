package com.ataglance.walletglance.data.utils

import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionType
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithCategories
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithIds
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionsWithIds
import com.ataglance.walletglance.domain.entities.CategoryCollection
import com.ataglance.walletglance.domain.entities.CategoryCollectionCategoryAssociation


fun transformCategCollectionsAndCollectionCategAssociationsToCollectionsWithIds(
    collectionList: List<CategoryCollection>,
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


fun CategoryCollectionType.toggle(): CategoryCollectionType {
    return when (this) {
        CategoryCollectionType.Expense -> CategoryCollectionType.Income
        CategoryCollectionType.Income -> CategoryCollectionType.Mixed
        CategoryCollectionType.Mixed -> CategoryCollectionType.Expense
    }
}


fun CategoryCollectionType.asChar(): Char {
    return when (this) {
        CategoryCollectionType.Expense -> '-'
        CategoryCollectionType.Income -> '+'
        CategoryCollectionType.Mixed -> 'm'
    }
}


fun List<CategoryCollectionWithIds>.breakOnCollectionsAndAssociations():
        Pair<List<CategoryCollection>, List<CategoryCollectionCategoryAssociation>>
{
    val categoryCollectionList = this.map { collectionWithIds ->
        CategoryCollection(
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


fun List<CategoryCollectionWithIds>.toCollectionsWithCategories(
    allCategories: List<Category>
): List<CategoryCollectionWithCategories> {
    return this.map { it.toCategoryCollectionWithCategories(allCategories) }
}


fun List<CategoryCollectionWithCategories>.toCollectionsWithIds(): List<CategoryCollectionWithIds> {
    return this.map { it.toCollectionWithIds() }
}
