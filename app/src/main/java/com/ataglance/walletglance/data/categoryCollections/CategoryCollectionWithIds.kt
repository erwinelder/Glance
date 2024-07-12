package com.ataglance.walletglance.data.categoryCollections

import com.ataglance.walletglance.data.categories.Category

data class CategoryCollectionWithIds(
    val id: Int = 0,
    val orderNum: Int = 0,
    val type: CategoryCollectionType = CategoryCollectionType.Expense,
    val name: String = "",
    val categoriesIds: List<Int>? = null
) {

    fun toCategoryCollectionWithCategories(
        allCategories: List<Category>
    ): CategoryCollectionWithCategories {

        return CategoryCollectionWithCategories(
            id = id,
            orderNum = orderNum,
            type = type,
            name = name,
            categoryList = categoriesIds?.let { categoriesIds ->
                allCategories.filter { category ->
                    categoriesIds.find { it == category.id } != null
                }
            }
        )
    }

}