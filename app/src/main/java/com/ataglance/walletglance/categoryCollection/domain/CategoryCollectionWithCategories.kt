package com.ataglance.walletglance.categoryCollection.domain

import com.ataglance.walletglance.category.domain.Category

data class CategoryCollectionWithCategories(
    val id: Int = 0,
    val orderNum: Int = 0,
    val type: CategoryCollectionType = CategoryCollectionType.Mixed,
    val name: String = "",
    val categoryList: List<Category>? = null
) {

    fun allowSaving(): Boolean {
        return name.isNotBlank()
    }

    fun toCollectionWithIds(): CategoryCollectionWithIds {
        return CategoryCollectionWithIds(
            id = id,
            orderNum = orderNum,
            type = type,
            name = name,
            categoriesIds = categoryList?.map { it.id }
        )
    }

}