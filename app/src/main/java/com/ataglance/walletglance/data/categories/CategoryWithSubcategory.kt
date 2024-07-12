package com.ataglance.walletglance.data.categories

import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithIds

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

}
