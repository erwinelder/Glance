package com.ataglance.walletglance.data.categoryCollections

data class CategoryCollectionsWithCategories(
    val expense: List<CategoryCollectionWithCategories> = listOf(),
    val income: List<CategoryCollectionWithCategories> = listOf(),
    val mixed: List<CategoryCollectionWithCategories> = listOf()
) {

    fun getListByType(type: CategoryCollectionType): List<CategoryCollectionWithCategories> {
        return when (type) {
            CategoryCollectionType.Expense -> expense
            CategoryCollectionType.Income -> income
            CategoryCollectionType.Mixed -> mixed
        }
    }

    fun concatenateLists(): List<CategoryCollectionWithCategories> {
        return expense + income + mixed
    }

    fun cloneAndAddNewCollection(
        type: CategoryCollectionType,
        name: String
    ): CategoryCollectionsWithCategories {
        val newList = getListByType(type).let { list ->
            list + CategoryCollectionWithCategories(
                id = 0,
                orderNum = (list.maxOfOrNull { it.orderNum } ?: 0) + 1,
                type = type,
                name = name,
                categoryList = listOf()
            )
        }
        return when (type) {
            CategoryCollectionType.Expense -> this.copy(expense = newList)
            CategoryCollectionType.Income -> this.copy(income = newList)
            CategoryCollectionType.Mixed -> this.copy(mixed = newList)
        }
    }

    fun cloneAndDeleteCollectionByOrderNum(
        orderNum: Int,
        type: CategoryCollectionType
    ): CategoryCollectionsWithCategories {
        val newList = getListByType(type).filter { it.orderNum != orderNum }
        return when (type) {
            CategoryCollectionType.Expense -> this.copy(expense = newList)
            CategoryCollectionType.Income -> this.copy(income = newList)
            CategoryCollectionType.Mixed -> this.copy(mixed = newList)
        }
    }

}
