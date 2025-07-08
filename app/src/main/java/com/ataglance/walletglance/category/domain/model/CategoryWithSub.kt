package com.ataglance.walletglance.category.domain.model

data class CategoryWithSub(
    val category: Category,
    val subcategory: Category? = null
) {

    val categoryId: Int
        get() = category.id

    val subcategoryId: Int?
        get() = subcategory?.id


    fun getSubcategoryOrCategory(): Category {
        return subcategory ?: category
    }

    fun groupParentAndSubcategoryOrderNums(): Double {
        return category.orderNum.toDouble() +
                (subcategory?.orderNum?.let { it.toDouble() / 100 } ?: 0.0)
    }

}
