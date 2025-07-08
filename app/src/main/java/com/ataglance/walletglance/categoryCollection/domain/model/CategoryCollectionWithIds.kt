package com.ataglance.walletglance.categoryCollection.domain.model

import com.ataglance.walletglance.category.domain.model.Category

data class CategoryCollectionWithIds(
    val id: Int = 0,
    val orderNum: Int = 0,
    val type: CategoryCollectionType = CategoryCollectionType.Expense,
    val name: String = "",
    val categoryIds: List<Int>? = null
) {

    fun toCategoryCollectionWithCategories(
        allCategories: List<Category>
    ): CategoryCollectionWithCategories {

        return CategoryCollectionWithCategories(
            id = id,
            orderNum = orderNum,
            type = type,
            name = name,
            categories = categoryIds?.let { categoriesIds ->
                allCategories.filter { category ->
                    categoriesIds.find { it == category.id } != null
                }
            }
        )
    }

}