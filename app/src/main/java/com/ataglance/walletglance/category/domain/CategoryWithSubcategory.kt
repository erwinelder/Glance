package com.ataglance.walletglance.category.domain

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithIds

@Stable
data class CategoryWithSubcategory(
    val category: Category,
    val subcategory: Category? = null
) {

    fun getSubcategoryOrCategory(): Category {
        return subcategory ?: category
    }

    fun matchCollection(collection: CategoryCollectionWithIds): Boolean {
        return (subcategory?.id ?: category.id).let {
            collection.categoriesIds?.contains(it)
        } ?: false
    }

    fun groupParentAndSubcategoryOrderNums(): Double {
        return category.orderNum.toDouble() +
                (subcategory?.orderNum?.let { it.toDouble() / 100 } ?: 0.0)
    }

}
