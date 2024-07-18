package com.ataglance.walletglance.data.categoryCollections

import com.ataglance.walletglance.data.utils.deleteItemAndMoveOrderNum

data class CategoryCollectionsWithCategories(
    val expense: List<CategoryCollectionWithCategories> = listOf(),
    val income: List<CategoryCollectionWithCategories> = listOf(),
    val mixed: List<CategoryCollectionWithCategories> = listOf()
) {

    fun getByType(type: CategoryCollectionType): List<CategoryCollectionWithCategories> {
        return when (type) {
            CategoryCollectionType.Expense -> expense
            CategoryCollectionType.Income -> income
            CategoryCollectionType.Mixed -> mixed
        }
    }

    private fun replaceListByType(
        list: List<CategoryCollectionWithCategories>,
        type: CategoryCollectionType
    ): CategoryCollectionsWithCategories {
        return when (type) {
            CategoryCollectionType.Expense -> this.copy(expense = list)
            CategoryCollectionType.Income -> this.copy(income = list)
            CategoryCollectionType.Mixed -> this.copy(mixed = list)
        }
    }

    fun concatenateLists(): List<CategoryCollectionWithCategories> {
        return expense + income + mixed
    }

    fun addCollection(
        collection: CategoryCollectionWithCategories
    ): CategoryCollectionsWithCategories {
        val newList = getByType(collection.type).toMutableList()
        newList.add(
            collection.copy(
                id = (concatenateLists().maxOfOrNull { it.id } ?: 0) + 1,
                orderNum = (newList.maxOfOrNull { it.orderNum } ?: 0) + 1
            )
        )
        return replaceListByType(newList, collection.type)
    }

    fun replaceCollection(
        collection: CategoryCollectionWithCategories
    ): CategoryCollectionsWithCategories {
        val newList = getByType(collection.type).map {
            it.takeIf { it.orderNum != collection.orderNum } ?: collection
        }
        return replaceListByType(newList, collection.type)
    }

    fun deleteCollection(
        collection: CategoryCollectionWithCategories,
    ): CategoryCollectionsWithCategories {
        val newList = getByType(collection.type).deleteItemAndMoveOrderNum(
            { it.orderNum == collection.orderNum }, { it.copy(orderNum = it.orderNum - 1) }
        )
        return replaceListByType(newList, collection.type)
    }

}
