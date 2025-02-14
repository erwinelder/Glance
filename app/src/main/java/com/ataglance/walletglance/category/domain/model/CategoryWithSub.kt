package com.ataglance.walletglance.category.domain.model

import androidx.compose.runtime.Stable

@Stable
data class CategoryWithSub(
    val category: Category,
    val subcategory: Category? = null
) {

    fun getSubcategoryOrCategory(): Category {
        return subcategory ?: category
    }

    fun match(categoryWithSub: CategoryWithSub): Boolean {
        return category.id == categoryWithSub.category.id &&
                subcategory?.id == categoryWithSub.subcategory?.id
    }

    fun matchIds(categoriesIds: List<Int>): Boolean {
        return categoriesIds.contains(subcategory?.id ?: category.id)
    }

    fun groupParentAndSubcategoryOrderNums(): Double {
        return category.orderNum.toDouble() +
                (subcategory?.orderNum?.let { it.toDouble() / 100 } ?: 0.0)
    }

}
