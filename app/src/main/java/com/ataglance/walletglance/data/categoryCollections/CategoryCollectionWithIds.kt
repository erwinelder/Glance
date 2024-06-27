package com.ataglance.walletglance.data.categoryCollections

import com.ataglance.walletglance.data.categories.Category

data class CategoryCollectionWithIds(
    val id: Int,
    val orderNum: Int,
    val type: CategoryCollectionType,
    val name: String,
    val categoriesIds: List<Int>?
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