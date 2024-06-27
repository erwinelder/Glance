package com.ataglance.walletglance.data.categoryCollections

import com.ataglance.walletglance.data.categories.Category

data class CategoryCollectionWithCategories(
    val id: Int,
    val orderNum: Int,
    val type: CategoryCollectionType,
    val name: String,
    val categoryList: List<Category>?
) {

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